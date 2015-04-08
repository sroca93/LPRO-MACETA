package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    ImageView imagenSeleccionada;
    Gallery gallery;
    public EntreFragments mCallback;
    private String userID;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public GalleryFragment() {
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
        Log.d("GalleryFragment", "entro");

        userID = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");

        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_gallery, container, false);
        imagenSeleccionada = (ImageView) rootView.findViewById(R.id.seleccionada);

        final ArrayList<String> imagenes = readFromDB();
        for (String i : imagenes) Log.d("imagen:", i);

        gallery = (Gallery) rootView.findViewById(R.id.gallery);
        gallery.setAdapter(new GalleryAdapter(this.getActivity(), imagenes));
        //al seleccionar una imagen, la mostramos en el centro de la pantalla a mayor tamaÃ±o

        //con este listener, sÃ³lo se mostrarÃ­an las imÃ¡genes sobre las que se pulsa
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                Log.d("hola", "gallery");
                ContentResolver cr = getActivity().getContentResolver();
                InputStream in = null;
                try {
                    in = cr.openInputStream(Uri.parse(imagenes.get(position)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeStream(in,null,options);

                imagenSeleccionada.setImageBitmap(bitmap);

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

    private ArrayList<String> readFromDB() {

        SQLite sqlu = new SQLite(this.getActivity(), "BDImagenes", null, 1);
        SQLiteDatabase db = sqlu.getWritableDatabase();
        ArrayList<String>paths = new ArrayList<String>();

        if(db != null)
        {
            String[] columns = {"ID", "path"};
            String[] args = new String[] {userID};

            Cursor c = db.query("Imagenes", columns, "userID=?", args, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    String pathFile = c.getString(1);
                    paths.add(pathFile);

                    Log.d("path:", pathFile);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return paths;
    }
}
