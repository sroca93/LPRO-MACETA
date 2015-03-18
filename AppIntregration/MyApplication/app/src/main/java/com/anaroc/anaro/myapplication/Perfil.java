package com.anaroc.anaro.myapplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import adapters.CustomListViewAdapter;
import adapters.CustomListViewAdapterTimeline;
import adapters.images.ImageDownloader;
import contenedores.Parametro;
import contenedores.Planta;
import contenedores.TimelineObject;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by guille on 20/02/15.
 */
public class Perfil extends Fragment{

    private String titulo="Vacio";
    private Planta plantaPerfil=new Planta();
    private final ImageDownloader imageDownloader = new ImageDownloader();
    private ImageView imagenplanta;
    private TextView textview;
    private View rootView;
    private ProgressDialog progDailog;
    private CustomListViewAdapter adapter;
    private ImageButton botonSeguir;
    private int lastBotonSeguir;
    public ListView listView;
    public boolean flag_back;
    private boolean flag_loading=false;
    private boolean flag_scroll_end;
    private boolean flag_first_time=true;
    private boolean flag_pulsado=false;
    public int ID_planta_seleccionada;
    private RatingBar ratingBarPerfil;
    public EntreFragments mCallback;
    public ImageButton botonEstadisticas;
    public ImageButton botonVideo;
    public ImageButton botonAmigo;
    private String myId; // = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
    private ArrayList<TimelineObject> items;
    private ArrayList<TimelineObject> itemsNuevos;
    private Button botonComent;
    private EditText editText;
    private Toast toast;
    private int top;
    private int index;
    private CustomListViewAdapterTimeline customAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lay_miplanta, container, false);
        textview = (TextView) rootView.findViewById(R.id.textViewMenuPersonaNombre);
        ratingBarPerfil= (RatingBar) rootView.findViewById(R.id.ratingBarPerfil);
        flag_scroll_end=false;
        this.editText = (EditText) rootView.findViewById(R.id.editText);
        botonSeguir = (ImageButton) rootView.findViewById(R.id.imageButton);
        botonSeguir.setVisibility(View.GONE);
        this.botonEstadisticas = (ImageButton) rootView.findViewById(R.id.imageButtonEstadisticas);
        this.botonEstadisticas.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        botonEstadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                storeState();
                mCallback.sendID_estdisticas(plantaPerfil.getIdPlanta());

            }
        });



        myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
        Log.d("MONMON  ", myId);
        String pass = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_PASSWORD_KEY", "");

        //myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
        //textview = (TextView) rootView.findViewById(R.id.textoTitulo);

        ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButtonCamara);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hola", "hola");
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("idPlanta", plantaPerfil.getIdPlanta());

                getActivity().startActivity(intent);
            }
        });


        imagenplanta = (ImageView) rootView.findViewById(R.id.imageViewMiPlanta);
        progDailog= new ProgressDialog(this.getActivity());
        if(this.plantaPerfil!=null) {



            imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imagenplanta);
            textview.setText(this.plantaPerfil.getTipo() +" de "+this.plantaPerfil.getDueno());
            String currentUser = PrefUtils.getFromPrefs(getActivity(),"ACTUAL_USERNAME","");
            String currentUserID = PrefUtils.getFromPrefs(getActivity(),"PREFS_LOGIN_USERNAME_KEY","");
            Log.d("MONDEBUG ", currentUser + " vs "+ this.plantaPerfil.getDueno());
            if (plantaPerfil.getDueno()!=null){
                if (this.plantaPerfil.getDueno().equalsIgnoreCase(currentUser)) {
                    //nada, es tu planta

                } else {

                    Log.d("MONDEBUG>>>", currentUserID + ", p " + ((plantaPerfil.getIdPlanta())));
                    Log.d("MONDEBUG>>>", plantaPerfil.toString());
                    new consultaIsFollowing().execute(new Parametro("consulta", "isFollowing"), new Parametro("myID", myId), new Parametro("plantID", (Integer.valueOf(this.plantaPerfil.getIdPlanta()).toString() )));
                }
                botonSeguir.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("MONMON  ", myId);
                        String plantID = (Integer.valueOf(plantaPerfil.getIdPlanta())).toString();
                        Log.d("MONMON ", plantID);
                        switch(lastBotonSeguir){
                            case R.drawable.seguir:
                                new consultaFollow().execute(new Parametro("consulta", "followPlant"), new Parametro("myID", myId), new Parametro("plantID", plantID ));
                                break;
                            case R.drawable.yaseguido:
                                new consultaUnfollow().execute(new Parametro("consulta", "unfollowPlant"), new Parametro("myID", myId), new Parametro("plantID", plantID ));
                                break;
                        }

                    }
                });




            }

            items = new ArrayList<TimelineObject>();

            listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
            additems();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                public void onScrollStateChanged(AbsListView view, int scrollState) {


                }

                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                    {
                        if(flag_loading == false && flag_scroll_end==false)
                        {
                            flag_loading = true;
                            additems();
                        }
                    }
                }
            });

            textview.setText(this.plantaPerfil.getTipo() +" de "+this.plantaPerfil.getDueno());

            botonComent =  (Button) rootView.findViewById(R.id.buttonComent);
            botonComent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(flag_pulsado==false)
                    {
                        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                        int dps = Math.round(pixels);
                        editText.setHeight(dps);
                        editText.getLayoutParams().height = dps;
                        botonComent.setText("Enviar");
                        flag_pulsado=true;
                    }
                    else
                    {
                        editText.setHeight(0);
                        editText.getLayoutParams().height = 0;
                        String textoNuevo=editText.getText().toString();
                        new ConsultaEnviaComent().execute(new Parametro("consulta","insertarComentario"),new Parametro("myID",myId),new Parametro("plantID",Integer.toString(plantaPerfil.getIdPlanta())),new Parametro("comentario",textoNuevo));
                        //toast.setText("Tu mensaje ha sido enviado correctamente");
                        //toast.show();
                        editText.setText("");
                        botonComent.setText("Comenta");
                        flag_pulsado=false;
                    }

                }
            });


            textview.setText(this.plantaPerfil.getTipo() +" de "+this.plantaPerfil.getDueno());

        }
        else
        {
            textview.setText("Planta=null");
        }



        return rootView;
    }

    public void setTitulo(String tituloNuevo){
        this.titulo=tituloNuevo;
    }

    public void setPlantaPerfil(Planta planta){
        this.plantaPerfil=planta;
    }


    public void cargarPerfilUsuario(String myId){
        new ConsultaPerfil().execute(new Parametro("consulta", "getMisPlantas"), new Parametro("myID", myId));

    }

    private void additems(){

        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        index = listView.getFirstVisiblePosition();

        new ConsultaTimeLine().execute(new Parametro("consulta", "getTimeline"), new Parametro("plantID", Integer.toString(plantaPerfil.getIdPlanta())), new Parametro("numero", Integer.toString(items.size() + 10)));


    }

    private void restoreState() {
        flag_back=true;
    }

    private void storeState(){
        flag_back=false;
    }

    public class ConsultaPerfil extends AsyncTask<Parametro, Void, Planta>
    {

        protected void onPreExecute() {

            /*progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected Planta doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            Planta[] respuestaParseada = Consultas.parsearPlantas(respuestaJSON);
            if(respuestaParseada.length>0) {
                return respuestaParseada[0];

            }
            else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(Planta planta) {

            if (planta != null) {
                plantaPerfil = planta;
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imagenplanta);
                textview.setText(plantaPerfil.getTipo() +" de "+plantaPerfil.getDueno());
                ratingBarPerfil.setRating(plantaPerfil.getValoracionMedia());
                //
                //if no es planta mia
                //checksilasigo
                //    si la sigo
                //       boton de dejar de seguir
                //    si no
                //       boton de seguir
                //else
                //    hide
                //botonSeguir.setVisibility(View.GONE);
                String currentUser = PrefUtils.getFromPrefs(getActivity(),"ACTUAL_USERNAME","");
                String currentUserID = PrefUtils.getFromPrefs(getActivity(),"PREFS_LOGIN_USERNAME_KEY","");
                Log.d("MONDEBUG ", currentUser + " vs "+ planta.getDueno());
                if(planta.getDueno().equalsIgnoreCase(currentUser)){
                    //nada, es tu planta

                }else{
                    //botonSeguir.setVisibility(View.VISIBLE);
                    Log.d("MONDEBUG>>>", currentUserID + ", p " + (Integer.valueOf(planta.getIdPlanta())).toString());
                    new consultaIsFollowing().execute(new Parametro("consulta", "isFollowing"), new Parametro("myID", myId ),  new Parametro("plantID", (Integer.valueOf(planta.getIdPlanta())).toString()));
                }

            }else{
                textview.setText("No hay plantas disponibles.");
                imagenplanta.setVisibility(View.GONE);
                ratingBarPerfil.setVisibility(View.GONE);
            }

            //progDailog.dismiss();
        }
    }

    public class ConsultaTimeLine extends AsyncTask<Parametro, Void, TimelineObject[]>
    {

        protected void onPreExecute() {

            /*progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected TimelineObject[] doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            TimelineObject[] respuestaParseada = Consultas.parsearTLObjects(respuestaJSON);
            TimelineObject[] TLObject = respuestaParseada;
            return TLObject;
        }

        @Override
        protected void onPostExecute(TimelineObject[] TLObject) {

            itemsNuevos=new ArrayList<TimelineObject>();
            if(TLObject.length % 10!=0)
            {
                flag_scroll_end=true;
            }
            for(int i=items.size(); i<TLObject.length ; i++)
            {
                itemsNuevos.add(TLObject[i]);
            }

            if(flag_first_time)
            {
                customAdapter = new CustomListViewAdapterTimeline(getActivity(), R.layout.lay_perfil_elemento_comentario, itemsNuevos);
                listView.setAdapter(customAdapter);
                flag_first_time=false;
            }
            else {
                customAdapter.addAll(itemsNuevos);
                customAdapter.notifyDataSetChanged();
                listView.setSelectionFromTop(index, top);
            }
            //progDailog.dismiss();
            items.addAll(itemsNuevos);
            flag_loading=false;

        }
    }

    public class ConsultaEnviaComent extends  AsyncTask<Parametro,Void,String>
    {
        protected void onPreExecute() {

                /*progDailog.setMessage("Cargando...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(true);
                progDailog.show();*/
        }

        @Override
        protected String doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            return(respuestaJSON);
        }

        @Override
        protected void onPostExecute(String respuestajson) {

            return;

        }
    }


    public class consultaIsFollowing extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 1) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.yaseguido);
                lastBotonSeguir = R.drawable.yaseguido;
                botonSeguir.setVisibility(View.VISIBLE);

            } else {
                //is not following!
                botonSeguir.setImageResource(R.drawable.seguir);
                lastBotonSeguir = R.drawable.seguir;
                botonSeguir.setVisibility(View.VISIBLE);
            }
        }

    }

    public class consultaFollow extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 0) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.yaseguido);
                lastBotonSeguir = R.drawable.yaseguido;
                //botonSeguir.setVisibility(View.VISIBLE);

            }
        }

    }

    public class consultaUnfollow extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 0) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.seguir);
                lastBotonSeguir = R.drawable.seguir;
                //botonSeguir.setVisibility(View.VISIBLE);

            }
        }

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (EntreFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

}
