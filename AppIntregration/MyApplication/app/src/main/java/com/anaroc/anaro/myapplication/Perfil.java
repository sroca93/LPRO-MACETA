package com.anaroc.anaro.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lay_miplanta, container, false);
        textview = (TextView) rootView.findViewById(R.id.textViewMenuPersonaNombre);
        imagenplanta = (ImageView) rootView.findViewById(R.id.imageViewMiPlanta);
        progDailog= new ProgressDialog(this.getActivity());
        if(this.plantaPerfil!=null) {

            /*GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(0, 1),
                    new DataPoint(1, 5),
                    new DataPoint(2, 3),
                    new DataPoint(3, 2),
                    new DataPoint(4, 6),
                    new DataPoint(5, 7),
                    new DataPoint(6, 8),
                    new DataPoint(7, 8),
                    new DataPoint(8, 7),
                    new DataPoint(9, 5)
            });
            graph.addSeries(series);*/

            imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imagenplanta);
            textview.setText(this.plantaPerfil.getTipo() +" de "+this.plantaPerfil.getDueno());



            final TimelineObject[] items = new TimelineObject[10];

            for (int i = 0; i < items.length; i++) {
                if (i == 4) {
                    items[i] = new TimelineObject(0,"coment","blablablabla","");
                } else if (i == 9) {
                    items[i] = new TimelineObject(1,"imagen","blablablabla","");
                } else if (i % 2 == 0) {
                    items[i] = new TimelineObject(0,"coment","blablablabla","");
                } else {
                    items[i] = new TimelineObject(1,"coment","blablablabla","");
                }
            }

            CustomListViewAdapterTimeline customAdapter = new CustomListViewAdapterTimeline(this.getActivity(), R.layout.lay_perfil_elemento_comentario, items);
            Log.i("jeje2: ",customAdapter.toString());
            listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
            listView.setAdapter(customAdapter);



            /*adapter = new CustomListViewAdapter(this.getActivity(),
                    R.layout.lay_perfil_elemento_comentario, new ArrayList<Planta>());
            listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
            listView.setAdapter(adapter);*/

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


    public void cargarPerfilUsuario(){
        new ConsultaPerfil().execute(new Parametro("consulta", "getPlantaAleatoriaParaValorar"), new Parametro("myID", "1"));


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
            }
            //progDailog.dismiss();
        }
    }
}