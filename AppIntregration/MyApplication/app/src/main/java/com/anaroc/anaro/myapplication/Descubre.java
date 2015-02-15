package com.anaroc.anaro.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Anaro on 10/02/2015.
 */
public class Descubre extends Fragment {
    private TextView respuesta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_descubre, container, false);
        respuesta = (TextView)rootView.findViewById(R.id.textView3);

        new Consulta(4, true, respuesta).execute("datos");
        return rootView;
    }
}
