package com.anaroc.anaro.myapplication;

/**
 * Created by guille on 5/03/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.CustomListViewAdapter;
import contenedores.Parametro;
import contenedores.Planta;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Followed extends Fragment {

    private Planta[] listaPlantas;
    private TextView respuesta;
    private CustomListViewAdapter adapter;
    private ProgressDialog progDailog;
    private RadioGroup radioButtonGroup;
    private Toast toast;
    private EntreFragments mCallback;
    public View rootView;
    public ListView listView;
    public boolean flag_back=true;
    private int index;
    private int top;
    private String myId;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(flag_back) {
            rootView = inflater.inflate(R.layout.lay_followed, container, false);
            adapter = new CustomListViewAdapter(this.getActivity(),
                    R.layout.lay_top_elementos, new ArrayList<Planta>());
            progDailog = new ProgressDialog(this.getActivity());
            myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
            new ConsultaFollowed().execute(new Parametro("consulta", "getPlantasQueSigo"), new Parametro("myID", (Integer.valueOf(myId.replace("\n",""))).toString()));
            listView = (ListView) rootView.findViewById(R.id.listview_followed);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(),(String) arrayAdapter.getItem(position),Toast.LENGTH_LONG).show();

                    //Intent intent = new Intent(getActivity(),PerfilFromTopActivity.class).putExtra("Planta", listaPlantas[position]);
                    //startActivity(intent);
                    storeState();
                    mCallback.sendPlanta(listaPlantas[position]);

                }
            });

            toast = Toast.makeText(this.getActivity(), "", Toast.LENGTH_SHORT);

            radioButtonGroup = new RadioGroup(this.getActivity());
            radioButtonGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup2);
            radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch(checkedId)
                    {
                        case R.id.radioButtonFollowed:
                            //new ConsultaTopFollowed().execute(new Parametro("consulta", "getTopPlantasQueSigo"), new Parametro("nombreUsuario", nombreUser));
                            new ConsultaFollowed().execute(new Parametro("consulta", "getPlantasQueSigo"), new Parametro("myID", (Integer.valueOf(myId.replace("\n",""))).toString()));
                            break;

                        case R.id.radioButtonBuscaUser:

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                            alert.setTitle("Buscar usuario");
                            alert.setMessage("Introduce el nombre del usuario");

                            // Set an EditText view to get user input
                            final EditText nameUser = new EditText(getActivity());
                            LinearLayout ll=new LinearLayout(getActivity());
                            ll.setOrientation(LinearLayout.VERTICAL);
                            ll.addView(nameUser);
                            alert.setView(ll);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String nombreUser = nameUser.getText().toString();
                                    new ConsultaUsuarioNombre().execute(new Parametro("consulta", "getPlantasPorUsuario"), new Parametro("nombreUsuario", nombreUser));

                                    // Do something with value!
                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                }
                            });

                            alert.show();

                            break;
                    }
                }
            });



        }
        else{
            restoreState();
        }
        return rootView;
    }

    private void restoreState() {
        flag_back=true;
        listView.setAdapter(adapter);
        listView.setSelectionFromTop(index, top);
    }

    private void storeState(){
        flag_back=false;
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
    }



    public class ConsultaUsuarioNombre extends AsyncTask<Parametro, Void, Planta[]>
    {

        protected void onPreExecute() {


            progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Planta[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);

            Log.i("AlvaroDEBUG: ", respuestaJSON);

            Planta[] respuestaParseada = Consultas.parsearPlantas(respuestaJSON);

            return respuestaParseada;

        }

        @Override
        protected void onPostExecute(Planta[] plantas) {
            listaPlantas = plantas;
            if (plantas.length>0) {
                adapter.clear();
                for (Planta planta : plantas)
                    adapter.add(planta);
            }else{
                Context context = getActivity();
                CharSequence text = "No se encuentran plantas de ese usuario";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            progDailog.dismiss();
        }
    }



    public class ConsultaFollowed extends AsyncTask<Parametro, Void, Planta[]>
    {

        protected void onPreExecute() {


            progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Planta[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);

            Planta[] respuestaParseada = Consultas.parsearPlantas(respuestaJSON);

            return respuestaParseada;

        }

        @Override
        protected void onPostExecute(Planta[] plantas) {
            listaPlantas = plantas;
            if (plantas.length>0) {
                adapter.clear();
                for (Planta planta : plantas)
                    adapter.add(planta);
            }else{
                Context context = getActivity();
                CharSequence text = "No sigues a ninguna planta";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            progDailog.dismiss();
        }
    }





    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (EntreFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }



}
