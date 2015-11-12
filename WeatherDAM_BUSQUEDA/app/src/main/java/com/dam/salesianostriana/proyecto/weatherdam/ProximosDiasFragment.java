package com.dam.salesianostriana.proyecto.weatherdam;


import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_cincodias.List;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_cincodias.OpenWeatherFiveDays;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProximosDiasFragment extends Fragment {

    /**
     * PARA QUE COJA LA CIUDAD HABRIA QUE PASARLE AL CONSTRUCTOR EL NOMBRE DE LA CIUDAD
     * Y EL NOMBRE RECOGIDO PASARSELA A LA URL DE AQUI PARA QUE BUSQUE
     */

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemCiudad> ciudades;

    Context vPrueba;
    private String nombre_ciudad;


    String url = "http://api.openweathermap.org/data/2.5/forecast?q=Madrid,Spain&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";

    public ProximosDiasFragment() {
        // Required empty public constructor
    }


    final String URL_BASE_IMG_WEATHER = "http://openweathermap.org/img/w/";
    final String EXTENSION_IMG_WEATHER = ".png";

    ProgressDialog progressDialog;

    TextView txtCiudad, txtFechaHora, txtTiempo, txtTemperatura, txtAmanecer, txtAnochecer, txtMaxima, txtMinima, txtHumedad;
    ImageView imgTiempo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cinco_dias, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        new GetDataTask().execute();

        return v;
    }

    private class GetDataTask extends AsyncTask<Void, Void, java.util.List<List>> {

        @Override
        protected java.util.List<List> doInBackground(Void... params) {

            java.util.List<List> result = null;
            URL url = null;

            try {
                url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=Olivares,Spain&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric");

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

                // url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+params[0]+"&types=(cities)&language=es_ES&key=AIzaSyAowGYjVlmA1XJl2EZNvKCVgNF8nPk8uHE");

                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));


                OpenWeatherFiveDays op = gson.fromJson(r, OpenWeatherFiveDays.class);

                result = op.getList();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(java.util.List<List> listWeather) {

            String URL_BASE_IMG_WEATHER = "http://openweathermap.org/img/w/";
            String EXTENSION_IMG_WEATHER = ".png";

            DateFormat dfFechayHora = new SimpleDateFormat("yyyy-MM-dd");
            dfFechayHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));

            DateFormat dfHora = new SimpleDateFormat("HH:mm");
            dfHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));


            if (listWeather != null) {

                ciudades = new ArrayList();
                for (int i = 0; i < listWeather.size(); i = i + 8) {

                    String url_base = URL_BASE_IMG_WEATHER + listWeather.get(i).getWeather().get(0).getIcon() + EXTENSION_IMG_WEATHER;
                    String dia_semana = Utils.getDiaSemana(listWeather.get(i).getDtTxt().substring(0, 10));
                    String fecha = listWeather.get(i).getDtTxt().substring(0, 11);
                    String temp_max = String.valueOf(listWeather.get(i).getMain().getTempMax());
                    String temp_min = String.valueOf(listWeather.get(i).getMain().getTempMin());

                    ciudades.add(new ItemCiudad(url_base, dia_semana,fecha, temp_max, temp_min));

                }
                //ciudades.add(new ItemCiudad(URL_BASE_IMG_WEATHER + listWeather.get(0).getWeather().get(0).getIcon() + EXTENSION_IMG_WEATHER, listWeather.get(0).getDtTxt().substring(0, 10)));
                mAdapter = new CiudadAdapter(ciudades);
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