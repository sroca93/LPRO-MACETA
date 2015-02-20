package com.anaroc.anaro.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import contenedores.Planta;

/**
 * Created by guille on 20/02/15.
 */
public class Perfil extends Fragment {

    public Perfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_perfil, container, false);

        Planta planta = (Planta) getActivity().getIntent().getSerializableExtra("Planta");

        TextView texto = (TextView) rootView.findViewById(R.id.textoEjemplo);
        texto.setText(planta.toString());



        return rootView;
    }


}