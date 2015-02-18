package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contenedores.Parametro;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Top extends Fragment {

    private TextView respuesta;
    ArrayAdapter<String> topListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_top, container, false);

        topListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.lay_top_elementos, R.id.lay_top_elementos_textview, new ArrayList());

        new ConsultaTop().execute(new Parametro("consulta", "getTopPlantas"), new Parametro("numeroDePlantas", "4"));

        ListView listView = (ListView) rootView.findViewById(R.id.listview_top);
        listView.setAdapter(topListAdapter);

        return rootView;
    }


    public class ConsultaTop extends AsyncTask<Parametro, Void, String[]>
    {
        @Override
        protected String[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);

            String[] respuestaParseada = parserJSONTop(respuestaJSON);

            return respuestaParseada;

        }

        private String[] parserJSONTop(String respuestaJSON) {
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

        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                topListAdapter.clear();
                for (String string : strings)
                    topListAdapter.add(string);
            }
        }
    }




}
