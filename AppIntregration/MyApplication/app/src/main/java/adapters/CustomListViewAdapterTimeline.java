package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anaroc.anaro.myapplication.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import adapters.images.ImageDownloader;
import contenedores.TimelineObject;

public class CustomListViewAdapterTimeline extends ArrayAdapter<TimelineObject>{
    public static final int COMENTARIO = 2;
    public static final int NUEVAFOTO = 1;
    //public static final int TYPE_WHITE = 2;
    //public static final int TYPE_BLACK = 3;
    private ArrayList<TimelineObject> objects;
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
        return objects.get(position).getTipo();
    }

    public CustomListViewAdapterTimeline(Context context, int resource, ArrayList<TimelineObject> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TimelineObject listViewItem = objects.get(position);
        //objects.remove(position);
        int listViewItemType = getItemViewType(position);
        if (true) {

            viewHolder = new ViewHolder();

            if (listViewItemType == COMENTARIO) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lay_perfil_elemento_comentario, null);
                viewHolder.texto=(TextView) convertView.findViewById(R.id.textViewTextoComent);
                viewHolder.titulo=(TextView) convertView.findViewById(R.id.textViewTituloComent);
                viewHolder.Img=(ImageView) convertView.findViewById(R.id.imageViewComent);
                viewHolder.Img.setImageResource(R.drawable.logo_final);
                //imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFotoPerfil&userID=" + listViewItem.getThumbnail(), viewHolder.Img);
                Log.d("ConsultaFotaza", "http://193.146.210.69/consultas.php?consulta=getFotoPerfil&userID=" + listViewItem.getThumbnail());
                viewHolder.texto.setText("Mensaje: " + listViewItem.getTexto());
                String timestamp=listViewItem.getTimestamp();
                String[] tssp=timestamp.split(" ");
                String[] sp1=tssp[0].split("-");
                String[] sp2=tssp[1].split(":");
                timestamp=sp2[0]+":"+sp2[1]+" "+sp1[2]+"/"+sp1[1]+"/"+sp1[0].substring(2);
                viewHolder.titulo.setText(listViewItem.getTitulo()+", "+timestamp);
                convertView.setTag(viewHolder);

            } else if (listViewItemType == NUEVAFOTO) {
                String timestamp=listViewItem.getTimestamp();
                String[] tssp=timestamp.split(" ");
                String[] sp1=tssp[0].split("-");
                String[] sp2=tssp[1].split(":");
                timestamp=sp2[0]+":"+sp2[1]+" "+sp1[2]+"/"+sp1[1]+"/"+sp1[0].substring(2);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lay_perfil_elemento_nuevafoto, null);
                viewHolder.texto=(TextView) convertView.findViewById(R.id.textViewNuevaFoto);
                viewHolder.Img=(ImageView) convertView.findViewById(R.id.imageViewNuevaFoto);

                viewHolder.texto.setText("Nueva foto subida - " +timestamp);
                imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+listViewItem.getThumbnail(), viewHolder.Img);
                convertView.setTag(viewHolder);

            } /*else if (listViewItemType == TYPE_WHITE) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.type_white, null);
            } */
            else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lay_perfil_elemento_nuevafoto, null);

            }


            assert convertView != null;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}