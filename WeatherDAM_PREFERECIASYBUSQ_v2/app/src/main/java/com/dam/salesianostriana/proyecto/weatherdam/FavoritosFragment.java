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

import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.OpenWeatherDaily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment {

    /**
     * PARA QUE COJA LA CIUDAD HABRIA QUE PASARLE AL CONSTRUCTOR EL NOMBRE DE LA CIUDAD
     * Y EL NOMBRE RECOGIDO PASARSELA A LA URL DE AQUI PARA QUE BUSQUE
     */

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemFavorito> listaFavoritos;

    Context vPrueba;
    private String nombre_ciudad;
    private String nombre_completo;

    String url = "http://api.openweathermap.org/data/2.5/forecast?q=Madrid,Spain&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";

    public FavoritosFragment() {
        // Required empty public constructor
    }


    final String URL_BASE_IMG_WEATHER = "http://openweathermap.org/img/w/";
    final String EXTENSION_IMG_WEATHER = ".png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        listaFavoritos = new ArrayList<ItemFavorito>();

        Utils.cargarArray(getActivity());

        listaFavoritos = new ArrayList<ItemFavorito>();

        for (int i = 0; i < Utils.listadoCiudadesFav.size(); i++) {
            //listaFavoritos.add(new ItemFavorito(Utils.listadoCiudadesFav.get(i)));
           new GetFavoritosTask().execute(Utils.listadoCiudadesFav.get(i));

        }

       /* mAdapter = new FavoritoAdapter(listaFavoritos);
        mRecyclerView.setAdapter(mAdapter);*/

        //new GetDataTask().execute();

        return v;
    }

    private class GetFavoritosTask extends AsyncTask<String, Void, OpenWeatherDaily> {

        @Override
        protected OpenWeatherDaily doInBackground(String... params) {

            //URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+nombre_ciudad.replace(" ","").trim()+"&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";
            String URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+params[0].replace(" ","%20").replace("España","Spain")+"&units=metric&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es";
            OpenWeatherDaily result = null;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    //return null;
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

        @Override
        protected void onPostExecute(OpenWeatherDaily openWeatherDaily) {

            DateFormat dfFechayHora = new SimpleDateFormat("  HH:mm\ndd/MM/yyyy");
            dfFechayHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));

            DateFormat dfHora = new SimpleDateFormat("HH:mm");
            dfHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));


            if (openWeatherDaily != null) {

                nombre_completo = openWeatherDaily.getName();
                String url_image = URL_BASE_IMG_WEATHER + openWeatherDaily.getWeather().get(0).getIcon() + EXTENSION_IMG_WEATHER;
                String temp = String.valueOf(openWeatherDaily.getMain().getTemp());

                listaFavoritos.add(new ItemFavorito(url_image, nombre_completo,temp));

                mAdapter = new FavoritoAdapter(listaFavoritos);
                mRecyclerView.setAdapter(mAdapter);


            } else {
                Toast.makeText(vPrueba, "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
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