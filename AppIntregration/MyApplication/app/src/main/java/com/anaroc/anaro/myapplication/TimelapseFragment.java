package com.anaroc.anaro.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

import contenedores.Parametro;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelapseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelapseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelapseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EntreFragments mCallback;
    private VideoView videoView;
    private boolean hasActiveHolder;
    private int idPlanta;
    private ProgressDialog progDialog;
    private String url;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelapseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelapseFragment newInstance(String param1, String param2) {
        TimelapseFragment fragment = new TimelapseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TimelapseFragment() {
        // Required empty public constructor
    }

    public void setIdPlanta(int idPlanta) {
        this.idPlanta = idPlanta;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timelapse, container, false);
        progDialog = new ProgressDialog(this.getActivity());
        new ConsultaTimeLapse().execute(new Parametro("consulta", "getTimelapse"),new Parametro("idPlanta", String.valueOf(idPlanta)));



        // Inflate the layout for this fragment

        //String url = "http://193.146.210.69/video.avi"; // your URL here
        videoView = (VideoView) rootView.findViewById(R.id.surfaceView);
        /*MediaPlayer mediaPlayer = new MediaPlayer();
        SurfaceHolder surfaceHolder = videoView.getHolder();

        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();*/



        return rootView;



    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            hasActiveHolder = false;

            synchronized(this)          {
                this.notifyAll();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            hasActiveHolder = true;
            this.notifyAll();
        }
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

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (EntreFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
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


    public class ConsultaTimeLapse extends AsyncTask<Parametro, Void, String> {

        protected void onPreExecute() {
            progDialog.setMessage("Cargando...");
            progDialog.setIndeterminate(false);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(true);
            progDialog.show();
        }

        @Override
        protected String doInBackground(Parametro... params) {
            return Consultas.hacerConsulta(params);
        }

        protected void onPostExecute(String respuesta) {

            url = respuesta.replaceAll("\\\\", "");
            Log.d(url, url);
            Uri vidUri = Uri.parse("http://193.146.210.69" + url.replaceAll("\"", ""));
            Log.d("uri", String.valueOf(vidUri));

            //videoView.setVideoURI(Uri.parse("http://193.146.210.69/video.avi"));


            MediaController vidControl = new MediaController(getActivity());
            vidControl.setAnchorView(videoView);

            videoView.setMediaController(vidControl);
            videoView.setVideoURI(vidUri);


            videoView.start();
            progDialog.dismiss();

        }
    }

    }
