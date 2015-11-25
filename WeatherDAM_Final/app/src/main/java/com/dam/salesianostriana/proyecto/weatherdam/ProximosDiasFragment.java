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

import com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas.CiudadAdapter;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_listas.ItemCiudad;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_cincodias.OpenWeatherFiveDays;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Url_apis;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Jesús Pallares.
 */
public class ProximosDiasFragment extends Fragment {

    //Servirán para inicializar los elementos relacinados con el Recycler.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemCiudad> ciudades;

    /*
     Recoge el contexto de este fragment.
     */
    Context contextoProximosDias;

    private String nombre_ciudad;


    public ProximosDiasFragment() {}

    public ProximosDiasFragment(String nombre_ciudad) {this.nombre_ciudad = nombre_ciudad;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cinco_dias, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Se ejecuta el asyntasck.
        new GetFiveDaysTask().execute(nombre_ciudad.replace(" ","%20").replace("España","Spain"));

        return v;
    }


    /////////////////////ASYNTASK QUE REALIZA LA CONSULTA DEL PRONOSTICO DE 5 DÍAS/////////////////////

    private class GetFiveDaysTask extends AsyncTask<String, Void, OpenWeatherFiveDays> {

        @Override
        protected OpenWeatherFiveDays doInBackground(String... params) {
            OpenWeatherFiveDays result = null;
            URL url = null;
            if(params!=null)

            try {
                url = new URL(Url_apis.URL_BASE_FIVEDAYS()+params[0].replace(" ","").replace("España","Spain")+Url_apis.URL_BASE_APPEND());

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
                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
                result = gson.fromJson(r, OpenWeatherFiveDays.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(OpenWeatherFiveDays listWeather) {
            if (listWeather != null) {

                ciudades = new ArrayList<ItemCiudad>();


                for (int i = 0; i < listWeather.getList().size(); i = i + 8) {

                    String url_image = Url_apis.URL_BASE_IMG_WEATHER() + listWeather.getList().get(i).getWeather().get(0).getIcon() + Url_apis.EXTENSION_IMG_WEATHER();
                    String dia_semana = Utils.getDiaSemana(listWeather.getList().get(i).getDtTxt().substring(0, 10));
                    String fecha = "Día "+listWeather.getList().get(i).getDtTxt().substring(8, 11);
                    String temp_max = String.valueOf(listWeather.getList().get(i).getMain().getTempMax());
                    String temp_min = String.valueOf(listWeather.getList().get(i).getMain().getTempMin());
                    String descrp = String.valueOf(listWeather.getList().get(i).getWeather().get(0).getDescription().toUpperCase());

                    ciudades.add(new ItemCiudad(url_image, dia_semana,fecha, temp_max, temp_min, descrp));
                }

                mAdapter = new CiudadAdapter(ciudades);
                mRecyclerView.setAdapter(mAdapter);

            } else {
                Toast.makeText(contextoProximosDias, "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
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