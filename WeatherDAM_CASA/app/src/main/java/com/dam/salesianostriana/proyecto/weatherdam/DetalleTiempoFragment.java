package com.dam.salesianostriana.proyecto.weatherdam;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.model.OpenWeatherDaily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleTiempoFragment extends Fragment {

    Context vPrueba;
    private String nombre_ciudad;
    public DetalleTiempoFragment() {
        // Required empty public constructor
    }

    public DetalleTiempoFragment(String nombre_ciudad) {
        this.nombre_ciudad = nombre_ciudad;
    }


    final String URL_BASE_IMG_WEATHER = "http://openweathermap.org/img/w/";
    final String EXTENSION_IMG_WEATHER = ".png";

    ProgressDialog progressDialog;

    TextView txtCiudad, txtFechaHora, txtTiempo, txtTemperatura, txtAmanecer, txtAnochecer, txtMaxima, txtMinima, txtHumedad;
    ImageView imgTiempo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vPrueba = container.getContext();
        View v = inflater.inflate(R.layout.fragment_tiempo_hoy, container, false);

        txtFechaHora = (TextView) v.findViewById(R.id.txtFechayHora);
        txtTiempo = (TextView) v.findViewById(R.id.txtTiempo);
        txtTemperatura = (TextView) v.findViewById(R.id.txtTemperatura);
        txtAmanecer = (TextView) v.findViewById(R.id.txtAmanecer);
        txtAnochecer = (TextView) v.findViewById(R.id.txtAnochecer);
        imgTiempo = (ImageView) v.findViewById(R.id.imgTiempo);
        txtMaxima = (TextView) v.findViewById(R.id.txtMaxima);
        txtMinima = (TextView) v.findViewById(R.id.txtMinima);
        txtHumedad = (TextView) v.findViewById(R.id.txtHumedad);
        // CON ESTA LÍNEA DE CÓDIGO INDICO QUE ESTE FRAGMENT
        // TIENE UN MENÚ DE OPCIONES QUE DEBE SOBREESCRIBIR AL DEL ACTIVITY
        setHasOptionsMenu(true);

        new GetDataTask().execute();
        return v;
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


    private class GetDataTask extends AsyncTask<Void, Void, OpenWeatherDaily> {

        @Override
        protected OpenWeatherDaily doInBackground(Void... params) {

            String URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+nombre_ciudad.replace(" ","").trim()+"&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";

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

            DateFormat dfFechayHora = new SimpleDateFormat("HH:mm dd/MM/yy");
            dfFechayHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));

            DateFormat dfHora = new SimpleDateFormat("HH:mm");
            dfHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));


            if (openWeatherDaily != null) {

                txtFechaHora.setText(dfFechayHora.format(openWeatherDaily.getDt()));
                txtTiempo.setText(openWeatherDaily.getWeather().get(0).getDescription().toUpperCase());
                txtTemperatura.setText(openWeatherDaily.getMain().getTemp() + "º");
                txtAmanecer.setText(dfHora.format(openWeatherDaily.getSys().getSunrise()));
                txtAnochecer.setText(dfHora.format(openWeatherDaily.getSys().getSunset()));
                txtMaxima.setText(openWeatherDaily.getMain().getTempMax() + "º");
                txtMinima.setText(openWeatherDaily.getMain().getTempMin() + "º");
                txtHumedad.setText(openWeatherDaily.getMain().getHumidity() + "%");

                String url_image = URL_BASE_IMG_WEATHER + openWeatherDaily.getWeather().get(0).getIcon() + EXTENSION_IMG_WEATHER;

                Picasso.with(vPrueba)
                        .load(url_image)
                        .fit()
                        .into(imgTiempo);

            } else {
                Toast.makeText(vPrueba, "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
            }
        }

    }
}