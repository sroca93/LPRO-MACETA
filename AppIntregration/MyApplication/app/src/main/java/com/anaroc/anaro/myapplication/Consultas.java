package com.anaroc.anaro.myapplication;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import contenedores.Parametro;
import contenedores.Planta;

/**
 * Created by Trabajo on 17/02/2015.
 */
public class Consultas {

    final static String LOG_TAG = "Consultas";

/*
    public static String getTopPlantas(int numero)
    {
        ArrayList<parametro> parametros = new ArrayList<parametro>();
        parametros.add(new parametro("consulta", "getTopPlantas"));
        parametros.add (new parametro("numeroDePlantas", String.valueOf(numero)));
        return hacerConsulta(parametros);
    }

    public static String getTopPlantasPorTipo(String tipo, int numero)
    {
        ArrayList<parametro> parametros = new ArrayList<parametro>();
        parametros.add(new parametro("consulta", "getTopPlantasPorTipo"));
        parametros.add (new parametro("tipoDePlanta", tipo));
        parametros.add (new parametro("numeroDePlantas", String.valueOf(numero)));
        return hacerConsulta(parametros);
    }

    public static String getTopPlantasComoLasMias(int numero, int IDUsuario)
    {
        ArrayList<parametro> parametros = new ArrayList<parametro>();
        parametros.add(new parametro("consulta", "getTopPlantasComoLasMias"));
        parametros.add (new parametro("numeroDePlantas", String.valueOf(numero)));
        parametros.add (new parametro("myID", String.valueOf(IDUsuario)));
        return hacerConsulta(parametros);
    }
*/


    public static String hacerConsulta(Parametro[] parametros)
    {
        String respuesta = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri.Builder builder = Uri.parse("http://193.146.210.69/consultas.php?").buildUpon();
            if (parametros != null) {
                for (Parametro parametro : parametros) {
                    builder.appendQueryParameter(parametro.nombreParametro, parametro.valor);
                }
            }

            URL url = new URL(builder.build().toString());

            Log.v(LOG_TAG, "Built URI: " + builder.build().toString());

            // Create the request, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                respuesta = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                respuesta = null;
            }
            respuesta = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            respuesta = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.v(LOG_TAG, respuesta);

        return respuesta;
    }

    public static Planta[] parsearPlantas (String plantasJson){
        Gson gson = new Gson();
        Log.d(">>>>>>>ADebugTag", "ValueJson: " + plantasJson);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(plantasJson).getAsJsonArray();
        Planta[] listaPlantas = gson.fromJson(array.toString(), Planta[].class);
        //Log.d(">>>>>>>ADebugTag", "ValuePostJson: " + listaPlantas[0]);
        return listaPlantas;
    }
}
