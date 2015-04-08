package com.anaroc.anaro.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Luis on 15/03/2015.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> titulos;
    private HashMap<String, String> contenido;
    private Context context;

    public MyExpandableListAdapter(ArrayList<String> titulos, HashMap<String, String> contenido, Context context) {
        this.titulos = titulos;
        this.contenido = contenido;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return this.titulos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        String tit = this.titulos.get(groupPosition);
        switch (tit) {
            case "nombre_comun":
                return "Nombre común";

            case "nombre_latin":
                return "Nombre latino";

            case "nombre_completo":
                return "Nombre completo";

            case "planta":
                return "Planta";

            case "poda":
                return "Poda";

            case "enfermedades":
                return "Enfermedades";

            case "interesante":
                return "Interesante";

            case "cuidados":
                return "Cuidados";

            case "recoleccion":
                return "Recolección";

            case "flores":
                return "Flores";

            case "riego":
                return "Riego";

            case "crecimiento":
                return "Crecimiento";

            case "fertilzacion":
                return "Fertilización";

            case "T_media_max":
                return "Temperatura media máxima";

            case "T_media_min":
                return "Temperatura media mínima";

            case "T_max":
                return "Temperatura máxima";

            case "T_min":
                return "Temperatura mínima";

            case "fertilizante":
                return "Cantidad de fertilizante";

            case "sol":
                return "Luz solar";

            case "agua":
                return "Riego necesario";

            default:
                return tit;

        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.contenido.get(this.titulos.get(groupPosition));
        /*if (isInteger(cont)) {
            switch (cont) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                default:
                    break;

            }
        }*/
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

   /* public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;

    }*/

}