package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anaroc.anaro.myapplication.R;

import java.util.List;

import adapters.images.ImageDownloader;
import contenedores.Planta;
import contenedores.TimelineObject;

public class CustomListViewAdapterTimeline extends ArrayAdapter<TimelineObject>{
    public static final int COMENTARIO = 0;
    public static final int NUEVAFOTO = 1;
    //public static final int TYPE_WHITE = 2;
    //public static final int TYPE_BLACK = 3;
    private TimelineObject[] objects;
    private final ImageDownloader imageDownloader = new ImageDownloader();


    public class ViewHolder {
        ImageView Img;
        int tipo;
        TextView titulo;
        TextView texto;

    }




    @Override
    public int getViewTypeCount() {
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        return objects[position].getTipo();
    }

    public CustomListViewAdapterTimeline(Context context, int resource, TimelineObject[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TimelineObject listViewItem = objects[position];
        int listViewItemType = getItemViewType(position);
        if (convertView == null) {

            viewHolder = new ViewHolder();

            if (listViewItemType == COMENTARIO) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lay_perfil_elemento_comentario, null);
                viewHolder.texto=(TextView) convertView.findViewById(R.id.textViewTextoComent);
                viewHolder.titulo=(TextView) convertView.findViewById(R.id.textViewTituloComent);
                viewHolder.Img=(ImageView) convertView.findViewById(R.id.imageViewComent);
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+listViewItem.getThumbnail(), viewHolder.Img);

                viewHolder.texto.setText("Mensaje: " + listViewItem.getTexto());
                viewHolder.titulo.setText(listViewItem.getTitulo());
                viewHolder.Img.setImageResource(R.drawable.imagen_planta_dos);
            } else if (listViewItemType == NUEVAFOTO) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lay_perfil_elemento_nuevafoto, null);
                viewHolder.texto=(TextView) convertView.findViewById(R.id.textViewNuevaFoto);
                viewHolder.Img=(ImageView) convertView.findViewById(R.id.imageViewNuevaFoto);

                viewHolder.texto.setText("Novedad: " + listViewItem.getTexto());
                viewHolder.Img.setImageResource(R.drawable.imagen_planta_dos);

            } /*else if (listViewItemType == TYPE_WHITE) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.type_white, null);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.type_black, null);
            }*/

            assert convertView != null;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}