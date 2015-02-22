package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        plantaAleatoria=null;
        rootView = inflater.inflate(R.layout.lay_descubre, container, false);
        texto1=(TextView) rootView.findViewById(R.id.textViewDescubre3);
        imagenplanta= (ImageView)rootView.findViewById(R.id.imageViewDescubre);
        imagenplanta.setImageResource(R.drawable.imagen_planta_uno);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDescubre);


        this.boton1 = (Button) rootView.findViewById(R.id.botonDescubre1);
        boton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getActivity(),
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
                        ponerImagenAleatoria();
            }
        });

        //respuesta = (TextView)rootView.findViewById(R.id.textView3);
        //new Consulta(4, true, respuesta).execute("datos");
        return rootView;
    }



    public void ponerImagenAleatoria(){

        new ConsultaDescubre().execute(new Parametro("consulta", "getPlantaAleatoriaParaValorar"),new Parametro("myID", "1"));
        this.ratingBar.setRating(Float.parseFloat("2.5"));
        imagenplanta.setImageResource(R.drawable.imagen_planta_dos);
        //Log.d(">>>>>>>ADebugTag", "Value: " + plantaAleatoria);


    }


    public class ConsultaDescubre extends AsyncTask<Parametro, Void, Planta[]>
    {
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
            plantaAleatoria = plantas[0];
            if (plantas != null) {
                texto1.setText(plantaAleatoria.getTipo());

            }

        }
    }


}