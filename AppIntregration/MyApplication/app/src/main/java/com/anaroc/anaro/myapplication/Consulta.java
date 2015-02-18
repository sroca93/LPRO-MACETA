package com.anaroc.anaro.myapplication;

/**
 * Created by Luis on 15/02/2015.
 */

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Consulta  extends AsyncTask<String,Void,String>{

    private int caso;
    private TextView respuesta;
    private boolean byGetOrPost = false;
    //flag 0 means get and 1 means post.(By default it is get.)
    public Consulta(int caso, boolean flag, TextView respuesta) {
        this.caso = caso;
        byGetOrPost = flag;
        this.respuesta = respuesta;
    }

    protected void onPreExecute(){

    }

    @Override

    protected String doInBackground(String... arg0) {
        switch (caso) {
            case 1:
                return hacerConsulta("getTopPlantas", new String[]{"numeroDePlantas", "3"});
            case 2:
                return hacerConsulta("getUsersQueMeSiguen", new String[]{"myID", "1"});
            case 3:
                return  hacerConsulta("getPlantasQueSigo", new String[]{"myID", "1"});
            case 4:
                hacerConsulta("getPlantasQueSigo", new String[]{"myID", "1"});
	     }
        return null;
    }

    protected String hacerConsulta (String nombreConsulta, String[] parametros)
    {
        try {

            String data = URLEncoder.encode("consulta", "UTF-8")
                    + "=" + URLEncoder.encode(nombreConsulta, "UTF-8");
            if (parametros != null)
                data += "&" + URLEncoder.encode(parametros[0], "UTF-8")
                        + "=" + URLEncoder.encode(parametros[1], "UTF-8");
            String link = "http://193.146.210.69/consultas.php";
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result){
        this.respuesta.setText(result);
    }
}
