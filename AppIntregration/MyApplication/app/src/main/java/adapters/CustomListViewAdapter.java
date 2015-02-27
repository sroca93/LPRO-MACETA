package adapters;

import java.util.List;

import adapters.images.ImageDownloader;
import contenedores.Planta;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anaroc.anaro.myapplication.R;
import com.anaroc.anaro.myapplication.Top;

public class CustomListViewAdapter extends ArrayAdapter<Planta> {

    Context context;
    private final ImageDownloader imageDownloader = new ImageDownloader();
    public CustomListViewAdapter(Context context, int resourceId,
                                 List<Planta> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Planta rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lay_top_elementos, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText("Media: " + rowItem.getValoracionMedia());
        holder.txtTitle.setText( (position+1) + ". " + rowItem.getNombrePlanta());
        //holder.imageView.setImageResource(R.drawable.imagen_planta_uno);//rowItem.getImageId());
        imageDownloader.download("http://193.146.210.69/consultas.php?consulta=getFoto&url="+rowItem.getThumbnail(), holder.imageView);
        return convertView;
    }
}
