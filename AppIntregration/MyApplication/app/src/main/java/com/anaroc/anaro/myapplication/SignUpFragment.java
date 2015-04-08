package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import contenedores.Parametro;
import contenedores.Planta;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private Button botonReg;
    private OnFragmentInteractionListener mListener;
    public View rootView;
    private EditText user, pass;
    private String usuario, contra;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();

        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        botonReg = (Button) rootView.findViewById(R.id.buttonSignUp);
        user = (EditText) rootView.findViewById(R.id.editTextNewUser);
        pass = (EditText) rootView.findViewById(R.id.editTextNewPass);
        Log.d("hola", "hola");

        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) user.setHint("");
                else user.setHint("Usuario");

            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) pass.setHint("");
                else pass.setHint("Contraseña");

            }
        });


        botonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = user.getText().toString();
                contra = pass.getText().toString();
                Log.d("hola", "hola");
                new Registro().execute(new Parametro("consulta", "storeUser"),new Parametro("myName", usuario),new Parametro("myPass", contra));
                Log.d("llamado", "llamado");

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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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


    public class Registro extends AsyncTask<Parametro, Void, String>
    {

        @Override
        protected String doInBackground(Parametro... params) {
            //int respuestaJSON = Integer.parseInt(String.valueOf(Consultas.hacerConsulta(params)));
            return (Consultas.hacerConsulta(params));


        }

        protected void onPostExecute(String success) {
            if (Integer.parseInt(success.replaceAll("\n",""))>0) {
                Log.d("uno", "uno");
                Toast.makeText(getActivity(), "Registro realizado", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("user", usuario);
                intent.putExtra("pass", contra);
                intent.putExtra("id", success);
                Log.d("MONDEBUG ", success);

                startActivity(intent);
            }
            else user.setError("El usuario introducido ya ha sido elegido");
        }

        protected void onCancelled() {

        }
    }

}
