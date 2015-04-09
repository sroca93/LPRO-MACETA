package com.anaroc.anaro.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import adapters.CustomListViewAdapter;
import adapters.CustomListViewAdapterTimeline;
import adapters.images.ImageDownloader;
import contenedores.Parametro;
import contenedores.Planta;
import contenedores.TimelineObject;

public class Perfil extends Fragment{


    private static final int RESULT_CANCELED = -1;
    private static final int RESULT_OK = 0;
    private String titulo="Vacio";
    private Planta plantaPerfil=new Planta();
    private final ImageDownloader imageDownloader = new ImageDownloader();
    private ImageView imagenplanta;
    private TextView textview;
    private View rootView;
    private ProgressDialog progDailog;
    private CustomListViewAdapter adapter;
    private ImageButton botonSeguir;
    private int lastBotonSeguir;
    public ListView listView;
    public boolean flag_back=true;
    private boolean flag_loading=false;
    private boolean flag_scroll_end;
    private boolean flag_first_time=true;
    private boolean flag_pulsado=false;
    public int ID_planta_seleccionada;
    private RatingBar ratingBarPerfil;
    public EntreFragments mCallback;
    public ImageButton botonEstadisticas;
    public ImageButton botonFoto;
    public ImageButton botonAmigo;
    //private String myId; // = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private Context context;
    private CameraUtils cu;
    public ImageButton botonVideo;
    public ImageButton botonNewPlanta;
    private String myId; // = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
    private int numItems;
    private ArrayList<TimelineObject> itemsNuevos;
    private Button botonComent;
    private EditText editText;
    private Toast toast;
    private int top;
    private int index;
    private CustomListViewAdapterTimeline customAdapter;
    private Planta[] listaPlantas;
    private int indexPlanta = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lay_miplanta, container, false);
        textview = (TextView) rootView.findViewById(R.id.textViewMenuPersonaNombre);
        ratingBarPerfil= (RatingBar) rootView.findViewById(R.id.ratingBarPerfil);
        botonFoto = (ImageButton) rootView.findViewById(R.id.imageButtonCamara);
        botonVideo = (ImageButton) rootView.findViewById(R.id.imageButtonVideo);
        context = this.getActivity();

        myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
        String flag = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_CAMERA_FLAG", "");

        if (flag.equals("1")) {
            Log.d("compruebo","tarta");
            new ConsultaPerfil().execute(new Parametro("consulta", "getMisPlantas"), new Parametro("myID", myId));
            PrefUtils.saveToPrefs(getActivity(), "PREFS_CAMERA_FLAG", "0");
        }
        flag_scroll_end=false;
        this.editText = (EditText) rootView.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(editText.getText().toString().length()>0)
                {
                    botonComent.setText("Enviar");
                }
                if(editText.getText().toString().length()<=0 && flag_pulsado)
                {
                    botonComent.setText("Cancelar");
                }
            }
        });
        botonSeguir = (ImageButton) rootView.findViewById(R.id.imageButton);
        botonSeguir.setVisibility(View.GONE);
        botonNewPlanta = (ImageButton) rootView.findViewById(R.id.imageButtonFlower);
        this.botonNewPlanta.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        botonNewPlanta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Nueva planta");
                alert.setMessage("Introduce su nombre, especie y color:");

// Set an EditText view to get user input
                final EditText nameET = new EditText(getActivity());
                final EditText typeET = new EditText(getActivity());
                //final EditText colorET = new EditText(getActivity());
                nameET.setHint("Nombre");
                typeET.setHint("Especie");
                final Spinner colorSP= new Spinner(getActivity());
                LinearLayout ll = new LinearLayout(getActivity());
                String[] arraySpinner = new String[] {"ninguno", "rojo", "lila","rosa","amarillo","naranja","azul", "blanco"};
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
                colorSP.setAdapter(spinnerAdapter);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(nameET);
                ll.addView(typeET);
                ll.addView(colorSP);
                alert.setView(ll);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = nameET.getText().toString();
                        String type = typeET.getText().toString();
                        //String color = colorET.getText().toString();
                        String color = (String) colorSP.getSelectedItem();
                        new ConsultaNewPlant().execute(new Parametro("consulta", "storePlant"), new Parametro("myId", myId), new Parametro("plantName", name.toString()), new Parametro("plantTipo", type.toString()), new Parametro("plantColor",color.toString()));

                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();


            }
        });
        botonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                TimelapseFragment fragment = new TimelapseFragment();
                fragment.setIdPlanta(plantaPerfil.getIdPlanta());
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
        this.botonEstadisticas = (ImageButton) rootView.findViewById(R.id.imageButtonEstadisticas);
        this.botonEstadisticas.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        botonEstadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                storeState();
                mCallback.sendID_estdisticas(plantaPerfil.getIdPlanta());

            }
        });
        this.botonFoto.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cu = new CameraUtils(context, plantaPerfil.getIdPlanta(),false);
                fileUri = cu.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i("MONDEBUG ", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("MONDEBUG ", "Right to Left");
                                cambiaPlanta(1);
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("MONDEBUG ", "Left to Right");
                                cambiaPlanta(-1);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
        Log.d("MONMON  ", myId);

        String pass = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_PASSWORD_KEY", "");

        //myId = PrefUtils.getFromPrefs(this.getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
        //textview = (TextView) rootView.findViewById(R.id.textoTitulo);

        ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButton_metro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.saveToPrefs(getActivity(), "PREFS_CAMERA_FLAG", "1");
                Log.d("hola", "hola");
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("idPlanta", plantaPerfil.getIdPlanta());

                getActivity().startActivity(intent);
            }
        });


        imagenplanta = (ImageView) rootView.findViewById(R.id.imageViewMiPlanta);
        imagenplanta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                final ImageView imV= new ImageView(getActivity());
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imV);
                LinearLayout ll=new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(imV);
                alert.setView(ll);

                alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();


            }
        });
        progDailog= new ProgressDialog(this.getActivity());

        if(this.plantaPerfil!=null ) {



            imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+plantaPerfil.getThumbnail(), imagenplanta);
            if(this.plantaPerfil.getTipo()!=null && this.plantaPerfil.getDueno()!=null && !this.plantaPerfil.getTipo().equals("null")){
                textview.setText(this.plantaPerfil.getTipo() +" de "+this.plantaPerfil.getDueno());
            }
            String currentUser = PrefUtils.getFromPrefs(getActivity(),"ACTUAL_USERNAME","");
            String currentUserID = PrefUtils.getFromPrefs(getActivity(),"PREFS_LOGIN_USERNAME_KEY","");
            Log.d("MONDEBUG ", currentUser + " vs "+ this.plantaPerfil.getDueno());
            ratingBarPerfil.setRating(plantaPerfil.getValoracionMedia());

            if (plantaPerfil.getDueno()!=null ){
                if (this.plantaPerfil.getDueno().equalsIgnoreCase(currentUser)) {
                    //nada, es tu planta
                    

                } else {
                    botonEstadisticas.setVisibility(View.GONE);
                    botonNewPlanta.setVisibility(View.GONE);
                    botonFoto.setVisibility(View.GONE);
                    botonVideo.setVisibility(View.GONE);
                    ImageButton butto = (ImageButton) rootView.findViewById(R.id.imageButton_metro);
                    butto.setVisibility(View.GONE);

                    ImageButton butt = (ImageButton) rootView.findViewById(R.id.imageButtonCamara);
                    butt.setVisibility(View.GONE);

                    Log.d("MONDEBUG>>>", currentUserID + ", p " + ((plantaPerfil.getIdPlanta())));
                    Log.d("MONDEBU    G>>>", plantaPerfil.toString());
                    new consultaIsFollowing().execute(new Parametro("consulta", "isFollowing"), new Parametro("myID", myId), new Parametro("plantID", (Integer.valueOf(this.plantaPerfil.getIdPlanta()).toString() )));

                    rootView.findViewById(R.id.diffTemperatura).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffLuminosidad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffHumedad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffHumedadSuelo).setVisibility(View.GONE);

                    rootView.findViewById(R.id.imagenHumedad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenHS).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenLuz).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenTemp).setVisibility(View.GONE);



                }
                this.botonSeguir.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                                v.invalidate();
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                break;
                            }
                        }
                        return false;
                    }
                });
                botonSeguir.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d("MONMON  ", myId);
                        String plantID = (Integer.valueOf(plantaPerfil.getIdPlanta())).toString();
                        Log.d("MONMON ", plantID);
                        switch(lastBotonSeguir){
                            case R.drawable.seguir:
                                new consultaFollow().execute(new Parametro("consulta", "followPlant"), new Parametro("myID", myId), new Parametro("plantID", plantID ));
                                break;
                            case R.drawable.yaseguido:
                                new consultaUnfollow().execute(new Parametro("consulta", "unfollowPlant"), new Parametro("myID", myId), new Parametro("plantID", plantID ));
                                break;
                        }

                    }
                });
            }

        }
        else
        {
            textview.setText("No hay plantas");
        }

        listView = (ListView) rootView.findViewById(R.id.listViewPerfil);
        if(flag_back) {
            numItems = 0;
            additems();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                public void onScrollStateChanged(AbsListView view, int scrollState) {


                }

                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (flag_loading == false && flag_scroll_end == false) {
                            flag_loading = true;
                            additems();
                        }
                    }
                }
            });
            if((this.plantaPerfil.getTipo()!=null)) {
                textview.setText(this.plantaPerfil.getTipo() + " de " + this.plantaPerfil.getDueno());
            }
        }
        else{
            restoreState();
        }

            botonComent =  (Button) rootView.findViewById(R.id.buttonComent);
            botonComent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(flag_pulsado==false)
                    {
                        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                        int dps = Math.round(pixels);
                        editText.setHeight(dps);
                        editText.getLayoutParams().height = dps;
                        botonComent.setText("Cancelar");
                        flag_pulsado=true;
                    }
                    else
                    {
                        editText.setHeight(0);
                        editText.getLayoutParams().height = 0;
                        String textoNuevo=editText.getText().toString();
                        if(!textoNuevo.equals("")) {
                            new ConsultaEnviaComent().execute(new Parametro("consulta", "insertarComentario"), new Parametro("myID", myId), new Parametro("plantID", Integer.toString(plantaPerfil.getIdPlanta())), new Parametro("comentario", textoNuevo));
                            //toast.setText("Tu mensaje ha sido enviado correctamente");
                            //toast.show();
                            numItems = 0;
                            if (customAdapter != null) {
                                customAdapter.clear();
                            }
                            additems();
                        }
                        botonComent.setText("Comenta");
                        flag_pulsado=false;
                        editText.setText("");

                    }

                }
            });

            if(this.plantaPerfil.getTipo()!=null) {
                textview.setText(this.plantaPerfil.getTipo() + " de " + this.plantaPerfil.getDueno());
            }





        return rootView;
    }

    public void setPlantaPerfil(Planta planta){
        this.plantaPerfil=planta;
    }


    public void cargarPerfilUsuario(String myId){
        new ConsultaPerfil().execute(new Parametro("consulta", "getMisPlantas"), new Parametro("myID", myId));

    }

    private void additems(){

        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        index = listView.getFirstVisiblePosition();

        new ConsultaTimeLine().execute(new Parametro("consulta", "getTimeline"), new Parametro("plantID", Integer.toString(plantaPerfil.getIdPlanta())), new Parametro("numero", Integer.toString(numItems + 10)));


    }



    private void restoreState() {
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        listView.setSelectionFromTop(index, top);
        flag_back=true;
    }

    private void storeState(){
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        index = listView.getFirstVisiblePosition();
        flag_back=false;
    }

    private void cambiaPlanta(int index) {
        if (listaPlantas.length > 0 && listaPlantas.length > (indexPlanta + index)) {

            indexPlanta += index;
            plantaPerfil = listaPlantas[indexPlanta];
            imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url=" + plantaPerfil.getThumbnail(), imagenplanta);
            textview.setText(plantaPerfil.getTipo() +" de "+plantaPerfil.getDueno()); // + " - "+ (indexPlanta+1) + " de "+ listaPlantas.length);
            ratingBarPerfil.setRating(plantaPerfil.getValoracionMedia());
            numItems=0;
            customAdapter.clear();
            additems();
            //
            //if no es planta mia
            //checksilasigo
            //    si la sigo
            //       boton de dejar de seguir
            //    si no
            //       boton de seguir
            //else
            //    hide
            //botonSeguir.setVisibility(View.GONE);
            String currentUser = PrefUtils.getFromPrefs(getActivity(), "ACTUAL_USERNAME", "");
            String currentUserID = PrefUtils.getFromPrefs(getActivity(), "PREFS_LOGIN_USERNAME_KEY", "");
            Log.d("MONDEBUG ", currentUser + " vs " + plantaPerfil.getDueno());
            if (plantaPerfil.getDueno().equalsIgnoreCase(currentUser)) {
                if (plantaPerfil.getTipo().trim().equals("Rosa"))
                    new consultaDiffOptimo().execute(new Parametro("consulta", "sonMisParametrosBuenos"), new Parametro("plantID", Integer.toString(plantaPerfil.getIdPlanta())));
                else {
                    rootView.findViewById(R.id.diffTemperatura).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffLuminosidad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffHumedad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.diffHumedadSuelo).setVisibility(View.GONE);

                    rootView.findViewById(R.id.imagenHumedad).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenHS).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenLuz).setVisibility(View.GONE);
                    rootView.findViewById(R.id.imagenTemp).setVisibility(View.GONE);
                }
            } else {
                //botonSeguir.setVisibility(View.VISIBLE);
                botonEstadisticas.setVisibility(View.GONE);
                botonNewPlanta.setVisibility(View.GONE);
                botonFoto.setVisibility(View.GONE);
                botonAmigo.setVisibility(View.GONE);
                botonVideo.setVisibility(View.GONE);
                ImageButton butto = (ImageButton) rootView.findViewById(R.id.imageButton_metro);
                butto.setVisibility(View.GONE);
                Log.d("MONDEBUG>>>", currentUserID + ", p " + (Integer.valueOf(plantaPerfil.getIdPlanta())).toString());
                new consultaIsFollowing().execute(new Parametro("consulta", "isFollowing"), new Parametro("myID", myId), new Parametro("plantID", (Integer.valueOf(plantaPerfil.getIdPlanta())).toString()));

                rootView.findViewById(R.id.diffTemperatura).setVisibility(View.GONE);
                rootView.findViewById(R.id.diffLuminosidad).setVisibility(View.GONE);
                rootView.findViewById(R.id.diffHumedad).setVisibility(View.GONE);
                rootView.findViewById(R.id.diffHumedadSuelo).setVisibility(View.GONE);

                rootView.findViewById(R.id.imagenHumedad).setVisibility(View.GONE);
                rootView.findViewById(R.id.imagenHS).setVisibility(View.GONE);
                rootView.findViewById(R.id.imagenLuz).setVisibility(View.GONE);
                rootView.findViewById(R.id.imagenTemp).setVisibility(View.GONE);

            }


        }else{
            if (listaPlantas.length <= 0) {
                textview.setText("No tienes ninguna planta");
                ratingBarPerfil.setVisibility(View.GONE);
                imagenplanta.setVisibility(View.GONE);
                botonComent.setVisibility(View.GONE);
                botonVideo.setVisibility(View.GONE);
                ImageButton butto = (ImageButton) rootView.findViewById(R.id.imageButton_metro);
                butto.setVisibility(View.GONE);
            }
            else{
                //No se hace nada
            }

        }
    }

    public class ConsultaPerfil extends AsyncTask<Parametro, Void, Planta[]>
    {

        protected void onPreExecute() {

            /*progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected Planta[] doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            Planta[] respuestaParseada = Consultas.parsearPlantas(respuestaJSON);
            if(respuestaParseada.length>0) {
                return respuestaParseada;

            }
            else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(Planta[] planta) {
            if (planta==null){
                textview.setText("No hay plantas disponibles.");
                imagenplanta.setVisibility(View.GONE);
                ratingBarPerfil.setVisibility(View.GONE);
                botonComent.setVisibility(View.GONE);
                botonEstadisticas.setVisibility(View.GONE);
                //botonNewPlanta.setVisibility(View.GONE);
                botonFoto.setVisibility(View.GONE);
                ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButtonCamara);
                button.setVisibility(View.GONE);
                botonSeguir.setVisibility(View.GONE);

                return;
            }
            if (planta.length>0) {
                listaPlantas = planta;
                indexPlanta = 0;
                plantaPerfil = planta[0];
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url=" + plantaPerfil.getThumbnail(), imagenplanta);
                textview.setText(plantaPerfil.getTipo() +" de "+plantaPerfil.getDueno()); // + " - "+ (indexPlanta+1) + " de "+ listaPlantas.length);
                ratingBarPerfil.setRating(plantaPerfil.getValoracionMedia());
                //
                //if no es planta mia
                //checksilasigo
                //    si la sigo
                //       boton de dejar de seguir
                //    si no
                //       boton de seguir
                //else
                //    hide
                //botonSeguir.setVisibility(View.GONE);
                String currentUser = PrefUtils.getFromPrefs(getActivity(),"ACTUAL_USERNAME","");
                String currentUserID = PrefUtils.getFromPrefs(getActivity(),"PREFS_LOGIN_USERNAME_KEY","");
                Log.d("MONDEBUG ", currentUser + " vs "+ plantaPerfil.getDueno());
                if(plantaPerfil.getDueno().equalsIgnoreCase(currentUser)){
                    if (plantaPerfil.getTipo().trim().equals("Rosa"))
                        new consultaDiffOptimo().execute(new Parametro("consulta", "sonMisParametrosBuenos"), new Parametro("plantID", Integer.toString(plantaPerfil.getIdPlanta())));
                    else {
                        rootView.findViewById(R.id.diffTemperatura).setVisibility(View.GONE);
                        rootView.findViewById(R.id.diffLuminosidad).setVisibility(View.GONE);
                        rootView.findViewById(R.id.diffHumedad).setVisibility(View.GONE);
                        rootView.findViewById(R.id.diffHumedadSuelo).setVisibility(View.GONE);

                        rootView.findViewById(R.id.imagenHumedad).setVisibility(View.GONE);
                        rootView.findViewById(R.id.imagenHS).setVisibility(View.GONE);
                        rootView.findViewById(R.id.imagenLuz).setVisibility(View.GONE);
                        rootView.findViewById(R.id.imagenTemp).setVisibility(View.GONE);
                    }
                }else{
                    //botonSeguir.setVisibility(View.VISIBLE);
                    botonEstadisticas.setVisibility(View.GONE);
                    botonNewPlanta.setVisibility(View.GONE);
                    botonFoto.setVisibility(View.GONE);
                    ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButtonCamara);
                    button.setVisibility(View.GONE);

                    Log.d("MONDEBUG>>>", currentUserID + ", p " + (Integer.valueOf(plantaPerfil.getIdPlanta())).toString());
                    new consultaIsFollowing().execute(new Parametro("consulta", "isFollowing"), new Parametro("myID", myId ),  new Parametro("plantID", (Integer.valueOf(plantaPerfil.getIdPlanta())).toString()));
                }
                additems();

            }else{
                textview.setText("No hay plantas disponibles.");
                imagenplanta.setVisibility(View.GONE);
                ratingBarPerfil.setVisibility(View.GONE);
            }

            Log.i("ALDEBUG",plantaPerfil.toString());

            //progDailog.dismiss();
        }
    }

    public class ConsultaNewPlant extends AsyncTask<Parametro, Void, Integer>
    {

        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            return Integer.parseInt(respuestaJSON.trim());

        }

        @Override
        protected void onPostExecute(Integer id) {
            if(id>0){
                Toast.makeText(getActivity(), "Planta creada con éxito",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Error al crear planta",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    public class ConsultaTimeLine extends AsyncTask<Parametro, Void, TimelineObject[]>
    {

        protected void onPreExecute() {

            /*progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected TimelineObject[] doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            TimelineObject[] respuestaParseada = Consultas.parsearTLObjects(respuestaJSON);
            TimelineObject[] TLObject = respuestaParseada;
            return TLObject;
        }

        @Override
        protected void onPostExecute(TimelineObject[] TLObject) {

            itemsNuevos=new ArrayList<TimelineObject>();
            if(TLObject.length % 10!=0 || (TLObject.length == numItems))
            {
                flag_scroll_end=true;
            }
            if(TLObject.length != numItems) {

                for (int i = numItems; i < TLObject.length ; i++) {
                    itemsNuevos.add(TLObject[i]);
                    Log.i("DEBUG_AL: ",TLObject[i].toString());
                }

                if (flag_first_time) {
                    customAdapter = new CustomListViewAdapterTimeline(getActivity(), R.layout.lay_perfil_elemento_comentario, itemsNuevos);
                    listView.setAdapter(customAdapter);
                    flag_first_time = false;
                } else {

                    customAdapter.addAll(itemsNuevos);
                    customAdapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(index, top);
                }
                //progDailog.dismiss();
                numItems=TLObject.length;
            }
            flag_loading=false;

        }
    }

    public class ConsultaEnviaComent extends  AsyncTask<Parametro,Void,String>
    {
        protected void onPreExecute() {

                /*progDailog.setMessage("Cargando...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(true);
                progDailog.show();*/
        }

        @Override
        protected String doInBackground(Parametro... params) {

            String respuestaJSON = Consultas.hacerConsulta(params);
            return(respuestaJSON);
        }

        @Override
        protected void onPostExecute(String respuestajson) {

            return;

        }
    }


    public class consultaIsFollowing extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 1) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.yaseguido);
                lastBotonSeguir = R.drawable.yaseguido;
                botonSeguir.setVisibility(View.VISIBLE);

            } else {
                //is not following!
                botonSeguir.setImageResource(R.drawable.seguir);
                lastBotonSeguir = R.drawable.seguir;
                botonSeguir.setVisibility(View.VISIBLE);
            }
        }

    }

    public class consultaFollow extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 0) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.yaseguido);
                lastBotonSeguir = R.drawable.yaseguido;
                //botonSeguir.setVisibility(View.VISIBLE);

            }
        }

    }

    public class consultaUnfollow extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String success) {

            //seguir.setVisibility(View.VISIBLE);
            Log.d("MONDEBUG>>>", success);
            if (Integer.parseInt(success.replaceAll("\n","")) == 0) {
                //isFollowing!

                botonSeguir.setImageResource(R.drawable.seguir);
                lastBotonSeguir = R.drawable.seguir;
                //botonSeguir.setVisibility(View.VISIBLE);

            }
        }

    }


    public class consultaDiffOptimo extends AsyncTask<Parametro, Void, String> {

        @Override
        protected String doInBackground(Parametro... params) {
            String respuestaJSON = (Consultas.hacerConsulta(params));
            String respuesta = respuestaJSON;
            return respuesta;
        }

        @Override
        protected void onPostExecute(String respuesta) {


            TextView textDiffTemp = (TextView) rootView.findViewById(R.id.diffTemperatura);
            TextView textDiffLum = (TextView) rootView.findViewById(R.id.diffLuminosidad);
            TextView textDiffHum = (TextView) rootView.findViewById(R.id.diffHumedad);
            TextView textDiffHumSuelo = (TextView) rootView.findViewById(R.id.diffHumedadSuelo);

            ImageView imageTemp = (ImageView) rootView.findViewById(R.id.imagenTemp);
            ImageView imageLuz = (ImageView) rootView.findViewById(R.id.imagenLuz);
            ImageView imageHS = (ImageView) rootView.findViewById(R.id.imagenHS);
            ImageView imageHum = (ImageView) rootView.findViewById(R.id.imagenHumedad);

            textDiffTemp.setVisibility(View.VISIBLE);
            textDiffLum.setVisibility(View.VISIBLE);
            textDiffHum.setVisibility(View.VISIBLE);
            textDiffHumSuelo.setVisibility(View.VISIBLE);

            imageTemp.setVisibility(View.VISIBLE);
            imageLuz.setVisibility(View.VISIBLE);
            imageHum.setVisibility(View.VISIBLE);
            imageHS.setVisibility(View.VISIBLE);


            Log.d(">>> Hinteligencia", respuesta);

            try {
                JSONObject obj = new JSONObject(respuesta);

                double diffTemp = obj.getDouble("diffTemp");
                double diffLum = obj.getDouble("diffLum");
                double diffHum = obj.getDouble("diffHum");
                double diffHumSuelo = obj.getDouble("diffHumSuelo");

                DecimalFormat myFormatter = new DecimalFormat("##%");


                textDiffTemp.setText(myFormatter.format(diffTemp));
                textDiffLum.setText(myFormatter.format(diffLum));
                textDiffHum.setText(myFormatter.format(diffHum));
                textDiffHumSuelo.setText(myFormatter.format(diffHumSuelo));

                if (Math.abs(diffTemp) > 1)
                    textDiffTemp.setTextColor(Color.RED);
                else
                    textDiffTemp.setTextColor(Color.GREEN);

                if (Math.abs(diffLum) > 1)
                    textDiffLum.setTextColor(Color.RED);
                else
                    textDiffLum.setTextColor(Color.GREEN);

                if (Math.abs(diffHum) > 1)
                    textDiffHum.setTextColor(Color.RED);
                else
                    textDiffHum.setTextColor(Color.GREEN);

                if (Math.abs(diffHumSuelo) > 1)
                    textDiffHumSuelo.setTextColor(Color.RED);
                else
                    textDiffHumSuelo.setTextColor(Color.GREEN);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (EntreFragments) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    /*private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        Toast.makeText(this.getActivity(), "File created: " + mediaFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

        return mediaFile;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            File f = new File(String.valueOf(fileUri).replaceAll("file:", ""));
            if (f.exists()) {
                savePhoto(fileUri);
                cu.uploadImage();
            }


        }

    }

    private void savePhoto (Uri fileUri) {
        SQLite sqlu = new SQLite(this.getActivity(), "BDImagenes", null, 1);
        SQLiteDatabase db = sqlu.getWritableDatabase();

        ContentValues nuevoRegistro = new ContentValues();
        Log.d("fileuri", String.valueOf(fileUri));
        nuevoRegistro.put("path", String.valueOf(fileUri));


        //Insertamos el registro en la base de datos
        db.insert("Imagenes", null, nuevoRegistro);
        db.close();
    }

}
