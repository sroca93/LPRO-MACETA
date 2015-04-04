package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListViewAdapter;
import contenedores.Parametro;
import contenedores.Planta;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Top extends Fragment {

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
            rootView = inflater.inflate(R.layout.lay_top, container, false);
            myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
            adapter = new CustomListViewAdapter(this.getActivity(),
                    R.layout.lay_top_elementos, new ArrayList<Planta>());
            progDailog = new ProgressDialog(this.getActivity());
            radioButtonGroup = new RadioGroup(this.getActivity());
            radioButtonGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
            radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch(checkedId)
                    {
                        case R.id.flowing:
                            new ConsultaTopFollowed().execute(new Parametro("consulta", "getTopPlantasQueSigo"), new Parametro("myID", myId));
                            break;
                        case R.id.mitipo:
                            new ConsultaTopMiTipo().execute(new Parametro("consulta", "getTopPlantasComoLasMias"),new Parametro("numeroDePlantas", "8") ,new Parametro("myID", myId));
                            break;
                        case R.id.todas:
                            new ConsultaTop().execute(new Parametro("consulta", "getTopPlantas"), new Parametro("numeroDePlantas", "8"));
                            break;
                    }
                }
            });



            new ConsultaTop().execute(new Parametro("consulta", "getTopPlantas"), new Parametro("numeroDePlantas", "5"));
            listView = (ListView) rootView.findViewById(R.id.listview_top);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(),(String) arrayAdapter.getItem(position),Toast.LENGTH_LONG).show();

                    //Intent intent = new Intent(getActivity(),PerfilFromTopActivity.class).putExtra("Planta", listaPlantas[position]);
                    //start++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Acçtivity(intent);
                    storeState();
                    mCallback.sendPlanta(listaPlantas[position]);

                }
            });

            toast = Toast.makeText(this.getActivity(), "", Toast.LENGTH_SHORT);

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


    public class ConsultaTopFollowed extends AsyncTask<Parametro, Void, Planta[]>
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


    public class ConsultaTopMiTipo extends AsyncTask<Parametro, Void, Planta[]>
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

            Log.i("AlvaroDEBUG: ",respuestaJSON);

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
                CharSequence text = "No se encuentran plantas de como la tuya";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            progDailog.dismiss();
        }
    }


    public class ConsultaTop extends AsyncTask<Parametro, Void, Planta[]>
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

        /*private String[] parserJSONTop(String respuestaJSON) {
            try {
                JSONArray arrayJSON = new JSONArray(respuestaJSON);

                int length = arrayJSON.length();
                String[] respuestaParseada = new String[length];

                for (int i = 0; i < length; i++) {
                    JSONObject objetoPlanta = arrayJSON.getJSONObject(i);
                    respuestaParseada[i] = objetoPlanta.getString("nombreUsuario") + " - " + objetoPlanta.getString("nombrePlanta") +
                            " - " + objetoPlanta.getString("ValoracionMedia");
                }

                return respuestaParseada;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return null;

        }*/

        @Override
        protected void onPostExecute(Planta[] plantas) {
            listaPlantas = plantas;
            if (plantas != null) {
                adapter.clear();
                for (Planta planta : plantas)
                    adapter.add(planta);
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
