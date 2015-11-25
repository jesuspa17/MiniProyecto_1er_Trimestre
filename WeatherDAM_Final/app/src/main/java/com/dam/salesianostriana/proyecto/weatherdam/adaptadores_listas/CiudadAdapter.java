package com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_listas.ItemCiudad;
import com.dam.salesianostriana.proyecto.weatherdam.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Clase que servirá como adaptador para la lista donde se mostrará la consulta de los 5 días.
 */

public class CiudadAdapter extends RecyclerView.Adapter<CiudadAdapter.ViewHolder> {

    private ArrayList<ItemCiudad> mDataset;
    Context contexto;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagenTiempo;
        public TextView dia_semana;
        public TextView fecha;
        public TextView tempMax;
        public TextView tempMin;
        public TextView descripcion;


        public ViewHolder(View v) {
            super(v);
            fecha = (TextView)v.findViewById(R.id.txtFecha);
            dia_semana = (TextView) v.findViewById(R.id.txtNombreCiudad);
            tempMax = (TextView)v.findViewById(R.id.txtMaxima);
            tempMin = (TextView)v.findViewById(R.id.txtMinima);
            descripcion = (TextView) v.findViewById(R.id.txtDescripcion);
            imagenTiempo  = (ImageView) v.findViewById(R.id.imgTiempoFav);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public CiudadAdapter(ArrayList<ItemCiudad> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CiudadAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_ciudad, viewGroup, false);

        contexto = v.getContext();

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CiudadAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.fecha.setText(mDataset.get(position).getFecha());
        holder.dia_semana.setText(mDataset.get(position).getDia_semana());
        holder.tempMax.setText(mDataset.get(position).getTemp_max());
        holder.tempMin.setText(mDataset.get(position).getTemp_min());
        holder.descripcion.setText(mDataset.get(position).getDescripcion());
        Picasso.with(contexto).load(mDataset.get(position).getImagen()).fit().into(holder.imagenTiempo);
    }

    @Override
    public int getItemCount()  {
        return mDataset.size();
    }
}
