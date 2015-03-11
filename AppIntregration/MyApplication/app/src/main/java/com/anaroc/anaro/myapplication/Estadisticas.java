package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Anaro on 09/03/2015.
 */




public class Estadisticas extends Fragment{

    private View rootView;
    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private int id_planta;


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

        GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph1);

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
            graph1.addSeries(series);

        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 7),
                new DataPoint(6, 8),
                new DataPoint(7, 0),
                new DataPoint(8, 7),
                new DataPoint(9, 5)
        });
        graph2.addSeries(series2);

        GraphView graph3 = (GraphView) rootView.findViewById(R.id.graph3);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 0),
                new DataPoint(4, 6),
                new DataPoint(5, 0),
                new DataPoint(6, 8),
                new DataPoint(7, 8),
                new DataPoint(8, 7),
                new DataPoint(9, 5)
        });
        graph3.addSeries(series3);


        return rootView;
    }
}
