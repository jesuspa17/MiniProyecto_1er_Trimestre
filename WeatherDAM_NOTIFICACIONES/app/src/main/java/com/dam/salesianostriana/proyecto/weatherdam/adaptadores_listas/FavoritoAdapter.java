package com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.salesianostriana.proyecto.weatherdam.MainActivity;
import com.dam.salesianostriana.proyecto.weatherdam.R;
import com.dam.salesianostriana.proyecto.weatherdam.notificaciones.GcmRegistrationAsyncTask;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_listas.ItemFavorito;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Clase que servirá como adaptador para la lista donde se mostrarán los favoritos que se vayan añadiendo.
 */

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.ViewHolder>{

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
        holder.temp.setText(mDataset.get(position).getTemperatura() + " º");
        Picasso.with(contexto).load(mDataset.get(position).getImagen()).fit().into(holder.imgTiempoFav);

        holder.lanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = contexto.getSharedPreferences("mispreferencias", Context.MODE_APPEND);
                Intent i = new Intent(contexto, MainActivity.class);
                i.putExtra("titulo", Utils.listadoCiudadesFav.get(position));
                if(prefs.getString("ciudad_defecto",null).equalsIgnoreCase(Utils.listadoCiudadesFav.get(position))){

                }else {
                    new GcmRegistrationAsyncTask(contexto).execute(Utils.listadoCiudadesFav.get(position));
                }
                contexto.startActivity(i);
                ((Activity) contexto).finish();
            }
        });
    }



    @Override
    public int getItemCount()  {
        return mDataset.size();
    }
}
