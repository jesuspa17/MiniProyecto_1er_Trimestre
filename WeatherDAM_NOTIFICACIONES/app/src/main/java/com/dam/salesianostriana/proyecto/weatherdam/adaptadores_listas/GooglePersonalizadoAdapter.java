package com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dam.salesianostriana.proyecto.weatherdam.R;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Predictions;

import java.util.List;

/**
 * @author Jesús Pallares on 14/11/2015.
 */
public class GooglePersonalizadoAdapter extends ArrayAdapter<Predictions> implements Filterable {

    List<Predictions> lista;
    Context contexto;

    public GooglePersonalizadoAdapter(Context context, List<Predictions> values) {
        super(context, -1, values);
        this.contexto = context;
        this.lista = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.list_item_places, parent, false);

        TextView nombreApTextView = (TextView) v.findViewById(R.id.textViewNombrePlaces);
        TextView txtPais = (TextView) v.findViewById(R.id.txtPaisPlaces);

        Predictions ciudadActual = lista.get(position);

        nombreApTextView.setText(ciudadActual.getDescription());
        String[] pais = ciudadActual.getDescription().split(",");
        if(pais.length==1){
            nombreApTextView.setText(pais[0]);
            txtPais.setText("");
        }else{
            nombreApTextView.setText(pais[0]);
            txtPais.setText(pais[1].replace(" ",""));
        }



        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    filterResults.values = lista;
                    filterResults.count = lista.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
