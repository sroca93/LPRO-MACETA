package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Descubre extends Fragment {
    private TextView respuesta;
    public View rootView;
    public RatingBar ratingBar;
    public Button boton1;
    public ImageView imagenplanta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lay_descubre, container, false);
        imagenplanta= (ImageView)rootView.findViewById(R.id.imageViewDescubre);
        imagenplanta.setImageResource(R.drawable.imagen_planta_uno);



        this.boton1 = (Button) rootView.findViewById(R.id.botonDescubre1);
        boton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDescubre);
                Toast.makeText(getActivity(),
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
                        ponerImagenAleatoria();
            }
        });

        respuesta = (TextView)rootView.findViewById(R.id.textView3);
        new Consulta(4, true, respuesta).execute("datos");
        return rootView;
    }



    public void ponerImagenAleatoria(){

        imagenplanta.setImageResource(R.drawable.imagen_planta_dos);

    }



}