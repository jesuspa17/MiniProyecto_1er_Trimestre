package com.dam.salesianostriana.proyecto.weatherdam;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas.FavoritoAdapter;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_listas.ItemFavorito;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.OpenWeatherDaily;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Url_apis;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment {

    //Servirán para inicializar los elementos relacinados con el Recycler.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /*
    Almacenará las ciudades que se obtengan de la lista de preferencias
     */
    private ArrayList<ItemFavorito> listaFavoritos;

    /*
    Recoge el contexto de este fragment.
     */
    Context contextoFavoritos;

    public FavoritosFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);

        //Se inicializan los elementos relacionados con el RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Se carga el array y se inicializa el array donde vamos a guardarlo
        Utils.cargarArray(getActivity());
        listaFavoritos = new ArrayList<ItemFavorito>();

        //Se recorre la lista estática de favoritos, y se van obteniendo los datos,
        //a través del asyntask
        for (int i = 0; i < Utils.listadoCiudadesFav.size(); i++) {
           new GetFavoritosTask().execute(Utils.listadoCiudadesFav.get(i));
        }

        return v;
    }

    ///////ASYNTASK QUE REALIZA LA CONSULTA PARA OBTENER LOS DATOS METEOROLÓGICOS DE UN DÍA PARA CADA FAVORITO///////

    private class GetFavoritosTask extends AsyncTask<String, Void, OpenWeatherDaily> {

        @Override
        protected OpenWeatherDaily doInBackground(String... params) {

            if(params!=null) {

                String URL_REQUEST = Url_apis.URL_BASE_REQUEST() + params[0].replace(" ", "%20").replace("España", "Spain") + Url_apis.URL_BASE_APPEND();
                OpenWeatherDaily result = null;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                        Long dateAsLong = json.getAsLong();
                        dateAsLong = dateAsLong * 1000L;
                        return new Date(dateAsLong);


                    }
                });

                Gson gson = gsonBuilder.create();

                try {
                    Reader r = Utils.Url2BufferedReader(URL_REQUEST);
                    result = gson.fromJson(r, OpenWeatherDaily.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            return null;
        }

        @Override
        protected void onPostExecute(OpenWeatherDaily openWeatherDaily) {

            if (openWeatherDaily != null) {

                String nombre_completo = openWeatherDaily.getName();
                String url_image = Url_apis.URL_BASE_IMG_WEATHER() + openWeatherDaily.getWeather().get(0).getIcon() + Url_apis.EXTENSION_IMG_WEATHER();
                String temp = String.valueOf(openWeatherDaily.getMain().getTemp());

                listaFavoritos.add(new ItemFavorito(url_image, nombre_completo,temp));

                mAdapter = new FavoritoAdapter(listaFavoritos);
                mRecyclerView.setAdapter(mAdapter);


            } else {
                Toast.makeText(contextoFavoritos, "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}