package com.example.pruebaphpsql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
            case R.id.topPlantas:
               return hacerConsulta("getTopPlantas", new String[]{"numeroDePlantas", "3"});
            case R.id.followers:
               return hacerConsulta("getUsersQueMeSiguen", new String[]{"myID", "1"});
            case R.id.followed:
              return  hacerConsulta("getPlantasQueSigo", new String[]{"myID", "1"});
            case R.id.descubre:
                return hacerConsulta("getPlantaAleatoriaParaValorar", new String[]{"myID", "1"});
		/*switch  (caso) {
		case 1:
			String consulta = "getTopPlantas";
			String numeroDePlantas = "2";
					String link="http://193.146.210.69/guille.php";

			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}*/


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
            String link = "http://193.146.210.69/guille.php";
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
            Log.d("tres", "tres");
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