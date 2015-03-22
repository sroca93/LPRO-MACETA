package com.anaroc.anaro.myapplication;

/**
 * Created by Luis on 21/03/2015.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> imagenes;
    int background;
    //guardamos las imÃ¡genes reescaladas para mejorar el rendimiento ya que estas operaciones son costosas
    //se usa SparseArray siguiendo la recomendaciÃ³n de Android Lint
    SparseArray<Bitmap> imagenesEscaladas = new SparseArray<Bitmap>();

    public GalleryAdapter(Context context, ArrayList<String> imagenes)
    {
        super();
        Log.d("GalleryAdapter", "entro");
        this.imagenes = imagenes;
        this.context = context;

        //establecemos un marco para las imÃ¡genes (estilo por defecto proporcionado)
        //por android y definido en /values/attr.xml
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Gallery1);
        background = typedArray.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        typedArray.recycle();
    }

    @Override
    public int getCount()
    {
        return imagenes.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imagen = new ImageView(context);

        //reescalamos la imagen para evitar "java.lang.OutOfMemory" en el caso de imÃ¡genes de gran resoluciÃ³n
        //como es este ejemplo
        if (imagenesEscaladas.get(position) == null)
        {

            ContentResolver cr = context.getContentResolver();
            InputStream in = null;
            try {
                in = cr.openInputStream(Uri.parse(imagenes.get(position)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 50;
            Bitmap bitmap = BitmapFactory.decodeStream(in,null,options);
            //Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), imagenes[position], 120, 0);
            imagenesEscaladas.put(position, bitmap);
        }
        imagen.setImageBitmap(imagenesEscaladas.get(position));
        //se aplica el estilo
        imagen.setBackgroundResource(background);

        return imagen;
    }

}
