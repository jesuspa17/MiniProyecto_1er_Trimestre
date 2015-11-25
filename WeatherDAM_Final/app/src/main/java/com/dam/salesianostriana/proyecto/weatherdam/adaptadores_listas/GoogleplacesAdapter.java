package com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Predictions;

import java.util.List;

/**
 * @author Jesús Pallares on 14/11/2015.
 *
 * Adaptador que se usará para pintar la lista de resultados que obtengamos de la consulta
 * realizada al api de google places.
 */
public class GoogleplacesAdapter extends ArrayAdapter implements Filterable {

    List<Predictions> lista;

        public GoogleplacesAdapter(Context context, int textViewResourceId, List<Predictions> lista) {
            super(context, textViewResourceId);
            this.lista = lista;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int index) {
            return lista.get(index).getDescription();
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
