package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.CustomListViewAdapter;
import contenedores.Parametro;
import contenedores.Planta;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlantAcademyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlantAcademyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantAcademyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public View rootView;
    private OnFragmentInteractionListener mListener;
    private Button boton;
    private EditText et;
    private ListView list;
    private EntreFragments mCallback;
    private InputMethodManager imm;
    private HashMap<String, Integer> hashlist = new HashMap<String, Integer>();
    private ProgressDialog progDialog;

    // TODO: Rename and change types and number of parameters
    public static PlantAcademyFragment newInstance(String param1, String param2) {
        PlantAcademyFragment fragment = new PlantAcademyFragment();

        return fragment;
    }

    public PlantAcademyFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_plant_academy, container, false);

        boton = (Button) rootView.findViewById(R.id.buttonBuscar);
        list = (ListView) rootView.findViewById(R.id.listViewPlantas);
        et = (EditText) rootView.findViewById(R.id.editTextPlanta);
        progDialog = new ProgressDialog(this.getActivity());

        imm  = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);


        list.setVisibility(View.GONE);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                new ConsultaPlantAcademy().execute(new Parametro("consulta", "busqueda_plantAcademy"),new Parametro("nombre_planta", et.getText().toString()));
            }
        });

        et.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                list.setVisibility(View.GONE);

                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                Log.d(item,item);
                int idPlanta = hashlist.get(item);
                Log.d("cosa", String.valueOf(idPlanta));
                FragmentManager fragmentManager = getFragmentManager();
                PlantaInfoFragment fragment = new PlantaInfoFragment();
                fragment.setIdPlanta(idPlanta);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("A_B_TAG")
                        .commit();
            }
        });


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

    public class ConsultaPlantAcademy extends AsyncTask<Parametro, Void, String[]> {

        protected void onPreExecute() {
            progDialog.setMessage("Cargando...");
            progDialog.setIndeterminate(false);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(true);
            progDialog.show();
        }

        @Override
        protected String[] doInBackground(Parametro... params) {
            String respuestaJSON = Consultas.hacerConsulta(params);

            ArrayList<String> lista = new ArrayList<String>();
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
                    lista.add(json_data.getString("nombre_completo"));
                    hashlist.put(json_data.getString("nombre_completo"), json_data.getInt("idPlanta"));

                    Log.i("log_tag", "nombre " + json_data.getString("nombre_completo") + json_data.getInt("idPlanta"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            String[] stringArray = lista.toArray(new String[lista.size()]);

            return stringArray;
        }

        protected void onPostExecute(String[] plantas) {
            list.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, plantas);

            progDialog.dismiss();

            // Assign adapter to ListView
            list.setAdapter(adapter);
        }
    }


}
