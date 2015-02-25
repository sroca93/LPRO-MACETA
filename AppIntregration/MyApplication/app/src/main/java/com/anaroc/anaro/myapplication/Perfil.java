package com.anaroc.anaro.myapplication;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import contenedores.Planta;

/**
 * Created by guille on 20/02/15.
 */
public class Perfil extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_miplanta, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
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
        graph.addSeries(series);

        ImageView imagenplanta= (ImageView)rootView.findViewById(R.id.imageViewMiPlanta);
        imagenplanta.setImageResource(R.drawable.imagen_planta_uno);


        //Planta planta = (Planta) getActivity().getIntent().getSerializableExtra("Planta");

        //TextView texto = (TextView) rootView.findViewById(R.id.textoTitulo);
        //texto.setText(planta.toString());



        return rootView;
    }


}