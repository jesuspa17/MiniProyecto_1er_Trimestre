package com.dam.salesianostriana.proyecto.weatherdam;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CiudadAdapter extends RecyclerView.Adapter<CiudadAdapter.ViewHolder> {

    //private String[] mDataset;
    private ArrayList<ItemCiudad> mDataset;
    Context contexto;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        static public ImageView imagenTiempo;
        public TextView dia_semana;
        public TextView fecha;
        public TextView tempMax;
        public TextView tempMin;

        public ViewHolder(View v) {
            super(v);
            fecha = (TextView)v.findViewById(R.id.txtFecha);
            dia_semana = (TextView) v.findViewById(R.id.txtDiaSemana);
            tempMax = (TextView)v.findViewById(R.id.txtMaxima);
            tempMin = (TextView)v.findViewById(R.id.txtMinima);
            imagenTiempo  = (ImageView) v.findViewById(R.id.imgTiempoList);
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
        Picasso.with(contexto).load(mDataset.get(position).getImagen()).fit().into(ViewHolder.imagenTiempo);
    }

    @Override
    public int getItemCount()  {
        return mDataset.size();
    }
}
