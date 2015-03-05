package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import adapters.images.ImageDownloader;
import contenedores.Parametro;
import contenedores.Planta;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Descubre extends Fragment {
    private TextView respuesta;
    public View rootView;
    public RatingBar ratingBar;
    public Button boton1;
    public ImageView imagenplanta;
    public Planta plantaAleatoria;
    public TextView texto1;
    private ProgressDialog progDailog;
    private final ImageDownloader imageDownloader = new ImageDownloader();
    private EntreFragments mCallback;
    private boolean flag_back=true;
    private float valoracion = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(flag_back) {
            plantaAleatoria = null;
            progDailog = new ProgressDialog(this.getActivity());
            rootView = inflater.inflate(R.layout.lay_descubre, container, false);
            texto1 = (TextView) rootView.findViewById(R.id.textViewDescubre3);
            imagenplanta = (ImageView) rootView.findViewById(R.id.imageViewDescubre);
            imagenplanta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    storeState();
                    mCallback.sendPlanta(plantaAleatoria);
                }
            });
            ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDescubre);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    valoracion = rating;
                }
            });
            ponerImagenAleatoria();


            this.boton1 = (Button) rootView.findViewById(R.id.botonDescubre1);
            boton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getActivity(),
                            String.valueOf(ratingBar.getRating()),
                            Toast.LENGTH_SHORT).show();


                    new AsyncTask<Parametro, Void, Void>() {
                        @Override
                        protected Void doInBackground(Parametro... params) {
                            Consultas.hacerConsulta(params);
                            return null;
                        }
                    }.execute(new Parametro("consulta", "insertarValoracion"),new Parametro("myID", "1"), new Parametro("plantID",
                            Integer.toString(plantaAleatoria.getIdPlanta())), new Parametro("valoracion", Float.toString(valoracion)));

                    ponerImagenAleatoria();
                }
            });

            //respuesta = (TextView)rootView.findViewById(R.id.textView3);
            //new Consulta(4, true, respuesta).execute("datos");
        }else{
            restoreState();
        }
        return rootView;

    }



    private void restoreState() {
        flag_back=true;
    }

    private void storeState(){
        flag_back=false;
    }


    public void ponerImagenAleatoria(){

        //FALTA PONER ID EN FUNCION DE PERSONA
        new ConsultaDescubre().execute(new Parametro("consulta", "getPlantaAleatoriaParaValorar"),new Parametro("myID", "1"));
        this.ratingBar.setRating(Float.parseFloat("2.5"));


    }


    public class ConsultaDescubre extends AsyncTask<Parametro, Void, Planta[]>
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
            plantaAleatoria = plantas[0];
            if (plantas != null) {
                texto1.setText(plantaAleatoria.getTipo()+" de "+plantaAleatoria.getDueno());
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaAleatoria.getThumbnail(), imagenplanta);

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