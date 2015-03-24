package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import contenedores.Parametro;
import contenedores.Planta;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlantaInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlantaInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantaInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
 
    private OnFragmentInteractionListener mListener;
    private int idPlanta;
    private View rootView;
    private ExpandableListView lista;
    private TextView titulo;
    private EntreFragments mCallback;
    private String title;
    private ArrayList<String> titulos;
    private HashMap<String, String> contenido;



    // TODO: Rename and change types and number of parameters
    public static PlantaInfoFragment newInstance(String param1, String param2) {
        PlantaInfoFragment fragment = new PlantaInfoFragment();
      
        return fragment;
    }

    public PlantaInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new ConsultaPlantAcademy().execute(new Parametro("consulta", "getPlantAcademy"),new Parametro("idPlanta", String.valueOf(idPlanta)));

        rootView = inflater.inflate(R.layout.fragment_planta_info, container, false);

        lista = (ExpandableListView) rootView.findViewById(R.id.expandableListViewPlanta);
        titulo = (TextView) rootView.findViewById(R.id.textViewTitle);
        titulos = new ArrayList<String>();
        contenido = new HashMap<String,String>();



        //titulo.setText(nombrePlanta);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
        try {
            mCallback = (EntreFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void setIdPlanta(int idPlanta) {
        this.idPlanta = idPlanta;
    }

    public class ConsultaPlantAcademy extends AsyncTask<Parametro, Void, String[]> {


        @Override
        protected String[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);
            Log.d("SDf", respuestaJSON);


            JSONArray json = null;
            try {
                json = new JSONArray(respuestaJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < json.length(); i++){
                JSONObject json_data = null;
                try {
                    json_data = json.getJSONObject(i);
                    if (!json_data.getString("nombre_comun").equals("null")){
                        titulos.add("nombre_comun");
                        contenido.put("nombre_comun", json_data.getString("nombre_comun"));
                        title = json_data.getString("nombre_comun");
                    }
                    if (!json_data.getString("nombre_completo").equals("null")){
                        titulos.add("nombre_completo");
                        contenido.put("nombre_completo", json_data.getString("nombre_completo"));
                    }
                    if (!json_data.getString("nombre_latin").equals("null")) {
                        titulos.add("nombre_latin");
                        contenido.put("nombre_latin", json_data.getString("nombre_latin"));
                    }
                    if (!json_data.getString("planta").equals("null")) {
                        titulos.add("planta");
                        contenido.put("planta", json_data.getString("planta"));
                    }
                    if (!json_data.getString("poda").equals("null"))  {
                        titulos.add("poda");
                        contenido.put("poda", json_data.getString("poda"));
                    }
                    if (!json_data.getString("enfermedades").equals("null")) {
                        titulos.add("enfermedades");
                        contenido.put("enfermedades", json_data.getString("enfermedades"));
                    }
                    if (!json_data.getString("interesante").equals("null")) {
                        titulos.add("interesante");
                        contenido.put("interesante", json_data.getString("interesante"));
                    }
                    if (!json_data.getString("cuidados").equals("null")) {
                        titulos.add("cuidados");
                        contenido.put("cuidados", json_data.getString("cuidados"));
                    }
                    if (!json_data.getString("recoleccion").equals("null")) {
                        titulos.add("recoleccion");
                        contenido.put("recoleccion", json_data.getString("recoleccion"));
                    }
                    if (!json_data.getString("flores").equals("null")) {
                        titulos.add("flores");
                        contenido.put("flores", json_data.getString("flores"));
                    }
                    if (!json_data.getString("riego").equals("null")) {
                        titulos.add("riego");
                        contenido.put("riego", json_data.getString("riego"));
                    }
                    if (!json_data.getString("crecimiento").equals("null")) {
                        titulos.add("crecimiento");
                        contenido.put("crecimiento", json_data.getString("crecimiento"));
                    }
                    if (!json_data.getString("fertilizacion").equals("null")) {
                        titulos.add("fertilizacion");
                        contenido.put("fertilizacion", json_data.getString("fertilizacion"));
                    }
                    if (!json_data.getString("T_media_max").equals("null"))  {
                        titulos.add("T_media_max");
                        contenido.put("T_media_max", json_data.getString("T_media_max"));
                    }
                    if (!json_data.getString("T_media_min").equals("null")) {
                        titulos.add("T_media_min");
                        contenido.put("T_media_min", json_data.getString("T_media_min"));
                    }
                    if (!json_data.getString("T_max").equals("null")) {
                        titulos.add("T_max");
                        contenido.put("T_max", json_data.getString("T_max") + "ºC");
                    }
                    if (!json_data.getString("T_min").equals("null")) {
                        titulos.add("T_min");
                        contenido.put("T_min", json_data.getString("T_min") + "ºC");
                    }
                    if (!json_data.getString("fertilizante").equals("null")) {
                        titulos.add("fertilizante");
                        contenido.put("fertilizante", json_data.getString("fertilizante"));
                    }
                    if (!json_data.getString("sol").equals("null")) {
                        titulos.add("sol");
                        contenido.put("sol", json_data.getString("sol"));
                    }
                    if (!json_data.getString("agua").equals("null")) {
                        titulos.add("agua");
                        contenido.put("agua", json_data.getString("agua"));
                    }


                    Log.i("log_tag", "nombre " + json_data.getString("nombre_completo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String[] stringArray = titulos.toArray(new String[titulos.size()]);


            return stringArray;

       }

        protected void onPostExecute(String[] plantas) {
            ExpandableListAdapter mAdapter;
            mAdapter = new MyExpandableListAdapter(titulos, contenido, getActivity());
            lista.setAdapter(mAdapter);
            titulo.setText(title);

        }
    }
    
}
