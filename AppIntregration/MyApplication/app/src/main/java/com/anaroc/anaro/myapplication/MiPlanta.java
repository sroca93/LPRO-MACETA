package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Anaro on 10/02/2015.
 */
public class MiPlanta extends Fragment {


 /*   /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_miplanta, container, false);
Log.d("uno", "uno");
        ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButton3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hola", "hola");
                Intent intent = new Intent(getActivity(), CameraActivity.class);


                getActivity().startActivity(intent);
            }
        });


        /*GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        ImageView imagenplanta= (ImageView)rootView.findViewById(R.id.imageViewMiPlanta);
        imagenplanta.setImageResource(R.drawable.imagen_planta_uno);

        return rootView;
    }
*/
}
