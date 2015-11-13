package com.dam.salesianostriana.proyecto.weatherdam;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.ViewHolder>{

    //private String[] mDataset;
    private ArrayList<ItemFavorito> mDataset;
    Context contexto;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView temp;
        public ImageView imgTiempoFav;
        public ImageButton btn_ver;
        public TextView lanzar;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView)v.findViewById(R.id.txtNomFav);
            temp = (TextView) v.findViewById(R.id.txtTemperaturaFav);
            imgTiempoFav = (ImageView) v.findViewById(R.id.imgTiempoFav);
            btn_ver = (ImageButton) v.findViewById(R.id.imgLanzar);
            lanzar = (TextView) v.findViewById(R.id.textViewLanzar);

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public FavoritoAdapter(ArrayList<ItemFavorito> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FavoritoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_favorito, viewGroup, false);

        contexto = v.getContext();

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final FavoritoAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText(mDataset.get(position).getNombre());
        holder.temp.setText(mDataset.get(position).getTemperatura()+" º");
        Picasso.with(contexto).load(mDataset.get(position).getImagen()).fit().into(holder.imgTiempoFav);

        holder.lanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(contexto, MainActivity.class);
                i.putExtra("titulo", Utils.listadoCiudadesFav.get(position));
                contexto.startActivity(i);
                ((Activity) contexto).finish();
            }
        });
        /*holder.btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(contexto, MainActivity.class);
                i.putExtra("titulo", Utils.listadoCiudadesFav.get(position));
                contexto.startActivity(i);
                ((Activity) contexto).finish();

            }
        });*/
    }



    @Override
    public int getItemCount()  {
        return mDataset.size();
    }
}
