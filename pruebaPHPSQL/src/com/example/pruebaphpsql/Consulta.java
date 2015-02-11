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
		Log.d("tres", "tres");

		String consulta = "getTopPlantas";
		String numeroDePlantas = "2";
		String link="http://193.146.210.69/guille.php";

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
		if(byGetOrPost){ //means by Get Method
			try{

				link = link + "?consulta="
						+consulta+"&numeroDePLantas="+numeroDePlantas;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));
				HttpResponse response = client.execute(request);
				BufferedReader in = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line="";
				while ((line = in.readLine()) != null) {
					sb.append(line);
					break;
				}
				in.close();
				return sb.toString();
			}catch(Exception e){
				return new String("Exception: " + e.getMessage());
			}
		}
		else{
			try{

				String data  = URLEncoder.encode("numeroDePlantas", "UTF-8")
						+ "=" + URLEncoder.encode(numeroDePlantas, "UTF-8");
				data  += "&" + URLEncoder.encode("consulta", "UTF-8")
						+ "=" + URLEncoder.encode(consulta, "UTF-8");
				URL url = new URL(link);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter
						(conn.getOutputStream());
				wr.write( data );
				wr.flush();
				BufferedReader reader = new BufferedReader
						(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				// Read Server Response
				while((line = reader.readLine()) != null)
				{
					sb.append(line);
					break;
				}
				return sb.toString();
			}catch(Exception e){
				return new String("Exception: " + e.getMessage());
			}
		}
	}
	@Override
	protected void onPostExecute(String result){
		this.respuesta.setText(result);
	}
}