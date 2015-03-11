package com.anaroc.anaro.myapplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public ListView listView;
    public boolean flag_back;
    public int ID_planta_seleccionada;
    private RatingBar ratingBarPerfil;
    public EntreFragments mCallback;
    public ImageButton botonEstadisticas;
    public ImageButton botonVideo;
    public ImageButton botonAmigo;
    private String myId; // = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lay_miplanta, container, false);
        textview = (TextView) rootView.findViewById(R.id.textViewMenuPersonaNombre);
        ratingBarPerfil= (RatingBar) rootView.findViewById(R.id.ratingBarPerfil);

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



            final TimelineObject[] items = new TimelineObject[10];


            // Dafuq.
            for (int i = 0; i < items.length; i++) {
                if (i == 1) {
                    items[i] = new TimelineObject(0,"Cuánto la riegas?","Simon","SimonPlanta1.jpg");
                } else if (i == 0) {
                    items[i] = new TimelineObject(1,"imagen","Has subido una foto hace 20m","");
                } else if (i == 2) {
                    items[i] = new TimelineObject(0,"Mola!","Tinki Winki","SimonPlanta1.jpg");
                } else {
                    items[i] = new TimelineObject(1,"Nueva foto para TimeLapse","","");
                }
            }


            CustomListViewAdapterTimeline customAdapter = new CustomListViewAdapterTimeline(this.getActivity(), R.layout.lay_perfil_elemento_comentario, items);
            listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
            listView.setAdapter(customAdapter);



            /*adapter = new CustomListViewAdapter(this.getActivity(),
                    R.layout.lay_perfil_elemento_comentario, new ArrayList<Planta>());
            listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
            listView.setAdapter(adapter);*/

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
            Planta planta = respuestaParseada[0];
            return planta;
        }

        @Override
        protected void onPostExecute(Planta planta) {

            if (planta != null) {
                plantaPerfil = planta;
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imagenplanta);
                textview.setText(plantaPerfil.getTipo() +" de "+plantaPerfil.getDueno());
                ratingBarPerfil.setRating(plantaPerfil.getValoracionMedia());
            }
            //progDailog.dismiss();
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
