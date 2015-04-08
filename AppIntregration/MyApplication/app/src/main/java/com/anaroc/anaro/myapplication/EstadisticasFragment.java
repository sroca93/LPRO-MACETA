package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import contenedores.Estadistica;
import contenedores.Parametro;
import contenedores.Planta;

/**
 * Created by Anaro on 09/03/2015.
 */




public class EstadisticasFragment extends Fragment {

    private View rootView;
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;

    private float humedad;
    private float temperatura;
    private float luminosidad;

    private int idPlanta;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.lay_estadisticas, container, false);
        textview1 = (TextView) rootView.findViewById(R.id.textViewPlot1);
        textview1.setText("Grafica Humedad");
        textview2 = (TextView) rootView.findViewById(R.id.textViewPlot2);
        textview2.setText("Grafica Luminosidad");
        textview3 = (TextView) rootView.findViewById(R.id.textViewPlot3);
        textview3.setText("Grafica Temperatura");

        new ConsultaEstadistica().execute(new Parametro("consulta", "obtenerMediciones"),
                new Parametro("plantID", Integer.toString(idPlanta)), new Parametro("numero", Integer.toString(3)));

        return rootView;
    }

    public void setIDPlanta (int idPlanta)
    {
        this.idPlanta = idPlanta;
    }


    public class ConsultaEstadistica extends AsyncTask<Parametro, Void, Estadistica[]> {

        @Override
        protected Estadistica[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);

            Estadistica[] respuestaParseada = parsearEstadisticas(respuestaJSON);

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

        public Estadistica[] parsearEstadisticas(String plantasJson) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(plantasJson).getAsJsonArray();
            Estadistica[] listaEstadisticas = gson.fromJson(array.toString(), Estadistica[].class);
            return listaEstadisticas;
        }

        @Override
        protected void onPostExecute(Estadistica[] estadisticas) {
            GraphView graphHumedad = (GraphView) rootView.findViewById(R.id.graph1);
            GraphView graphLuminosidad = (GraphView) rootView.findViewById(R.id.graph2);
            GraphView graphTemperatura = (GraphView) rootView.findViewById(R.id.graph3);

            ArrayList<DataPoint> datosHumedad = new ArrayList<DataPoint>();
            ArrayList<DataPoint> datosLuminosidad = new ArrayList<DataPoint>();
            ArrayList<DataPoint> datosTemperatura = new ArrayList<DataPoint>();


            for (int i = 0; i < estadisticas.length; i++) {
                Log.d("Estadistica", estadisticas[i].toString());
                datosHumedad.add(new DataPoint(i, Double.parseDouble(estadisticas[i].getHumedad())));
                datosLuminosidad.add(new DataPoint(i, Double.parseDouble(estadisticas[i].getLuminosidad())));
                datosTemperatura.add(new DataPoint(i, Double.parseDouble(estadisticas[i].getTemperatura())));
            }

            graphHumedad.addSeries(new LineGraphSeries<DataPoint>(datosHumedad.toArray(new DataPoint[]{})));
            graphLuminosidad.addSeries(new LineGraphSeries<DataPoint>(datosLuminosidad.toArray(new DataPoint[]{})));
            graphTemperatura.addSeries(new LineGraphSeries<DataPoint>( datosTemperatura.toArray(new DataPoint[]{})));


        }
    }
}