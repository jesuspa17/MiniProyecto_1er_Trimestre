package com.dam.salesianostriana.proyecto.weatherdam;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.OpenWeatherDaily;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleTiempoFragment extends Fragment {

    Context vPrueba;
    private String nombre_ciudad;

    public DetalleTiempoFragment() {

    }

    public DetalleTiempoFragment(String nombre_ciudad) {
        this.nombre_ciudad = nombre_ciudad;
    }



    TextView txtFechaHora, txtTiempo, txtTemperatura, txtAmanecer, txtAnochecer, txtMaxima, txtMinima, txtHumedad, txtActualizado;
    ImageView imgTiempo, imgAmanecer,imgAnochecer,imgTempMax,imgTempMin,imgHumedad;
    ImageView btn_fav;

    private String nombre_completo;

    SharedPreferences prefs;

    List<String> listaCiudades = new ArrayList<String>();

    boolean encencido;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vPrueba = container.getContext();
        View v = inflater.inflate(R.layout.fragment_tiempo_hoy, container, false);


        prefs = getActivity().getSharedPreferences("NuevasPreferencias", Context.MODE_APPEND);
        final SharedPreferences.Editor editor = prefs.edit();


        txtFechaHora = (TextView) v.findViewById(R.id.txtFechayHora);
        txtTiempo = (TextView) v.findViewById(R.id.txtTiempo);
        txtTemperatura = (TextView) v.findViewById(R.id.txtTemperatura);
        txtAmanecer = (TextView) v.findViewById(R.id.txtAmanecer);
        txtAnochecer = (TextView) v.findViewById(R.id.txtAnochecer);
        txtMaxima = (TextView) v.findViewById(R.id.txtMaxima);
        txtMinima = (TextView) v.findViewById(R.id.txtMinima);
        txtHumedad = (TextView) v.findViewById(R.id.txtHumedad);
        txtActualizado = (TextView) v.findViewById(R.id.textActualizado);

        btn_fav = (ImageView) v.findViewById(R.id.btn_fav);

        imgTiempo = (ImageView) v.findViewById(R.id.imgTiempoFav);
        imgAmanecer = (ImageView) v.findViewById(R.id.imageViewAm);
        imgAnochecer = (ImageView) v.findViewById(R.id.imageViewAno);
        imgTempMax = (ImageView) v.findViewById(R.id.imageViewMax);
        imgTempMin = (ImageView) v.findViewById(R.id.imageViewMin);
        imgHumedad = (ImageView) v.findViewById(R.id.imageViewHum);


        new GetDataTask().execute();

        Utils.cargarArray(getActivity());
        if(Utils.listadoCiudadesFav.contains(nombre_ciudad)){
            btn_fav.setImageResource(android.R.drawable.btn_star_big_on);
            encencido = true;
        }else{
            encencido = false;
        }


        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor mEdit1 = sp.edit();

        btn_fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(encencido){

                    Log.i("Tamanyo", String.valueOf(Utils.listadoCiudadesFav.size()));

                    if(Utils.listadoCiudadesFav.contains(nombre_ciudad)){
                        Utils.listadoCiudadesFav.remove(nombre_ciudad);
                    }
                    Utils.guardarArray(getActivity());

                    Snackbar.make(container, "Borrado de favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    btn_fav.setImageResource(android.R.drawable.btn_star_big_off);
                    encencido = false;

                }else{
                    if(!Utils.listadoCiudadesFav.contains(nombre_ciudad)) {

                        Utils.listadoCiudadesFav.add(nombre_ciudad);
                        Utils.guardarArray(getActivity());
                        Log.i("Añadido a favoritos", nombre_ciudad);

                        Snackbar.make(container, "Añadido a favoritos", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        btn_fav.setImageResource(android.R.drawable.btn_star_big_on);
                        encencido = true;
                    }


                }
            }
        });


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

            //URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+nombre_ciudad.replace(" ","").trim()+"&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";
            String URL_REQUEST = Utils.URL_BASE_REQUEST()+nombre_ciudad.replace(" ","%20").replace("España","Spain")+Utils.URL_BASE_APPEND();
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

                MainActivity.colocarTitulo(openWeatherDaily.getName());

                nombre_completo = openWeatherDaily.getName()+", "+openWeatherDaily.getSys().getCountry();

                txtFechaHora.setText(dfFechayHora.format(openWeatherDaily.getDt()));
                txtTiempo.setText(openWeatherDaily.getWeather().get(0).getDescription().toUpperCase());
                txtTemperatura.setText(openWeatherDaily.getMain().getTemp() + "º");
                txtAmanecer.setText(dfHora.format(openWeatherDaily.getSys().getSunrise()));
                txtAnochecer.setText(dfHora.format(openWeatherDaily.getSys().getSunset()));
                txtMaxima.setText(openWeatherDaily.getMain().getTempMax() + "º");
                txtMinima.setText(openWeatherDaily.getMain().getTempMin() + "º");
                txtHumedad.setText(openWeatherDaily.getMain().getHumidity() + "%");


                String url_image = Utils.URL_BASE_IMG_WEATHER() + openWeatherDaily.getWeather().get(0).getIcon() + Utils.EXTENSION_IMG_WEATHER();

                Picasso.with(vPrueba)
                        .load(url_image)
                        .fit()
                        .into(imgTiempo);

                mostrarElementosUI();
            } else {
                Toast.makeText(vPrueba, "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ocultarElementosUI();
        }
    }

    public void mostrarElementosUI(){

        txtFechaHora.setVisibility(View.VISIBLE);
        txtTiempo.setVisibility(View.VISIBLE);
        txtTemperatura.setVisibility(View.VISIBLE);
        txtAmanecer.setVisibility(View.VISIBLE);
        txtAnochecer.setVisibility(View.VISIBLE);
        txtMaxima.setVisibility(View.VISIBLE);
        txtMinima.setVisibility(View.VISIBLE);
        txtHumedad.setVisibility(View.VISIBLE);
        txtActualizado.setVisibility(View.VISIBLE);

        btn_fav.setVisibility(View.VISIBLE);

        imgTiempo.setVisibility(View.VISIBLE);
        imgTiempo.setVisibility(View.VISIBLE);
        imgAmanecer.setVisibility(View.VISIBLE);
        imgAnochecer.setVisibility(View.VISIBLE);
        imgTempMax.setVisibility(View.VISIBLE);
        imgTempMin.setVisibility(View.VISIBLE);
        imgHumedad.setVisibility(View.VISIBLE);
    }


    public void ocultarElementosUI(){

        txtActualizado.setVisibility(View.INVISIBLE);
        txtFechaHora.setVisibility(View.INVISIBLE);
        txtTiempo.setVisibility(View.INVISIBLE);
        txtTemperatura.setVisibility(View.INVISIBLE);
        txtAmanecer.setVisibility(View.INVISIBLE);
        txtAnochecer.setVisibility(View.INVISIBLE);
        txtMaxima.setVisibility(View.INVISIBLE);
        txtMinima.setVisibility(View.INVISIBLE);
        txtHumedad.setVisibility(View.INVISIBLE);

        btn_fav.setVisibility(View.INVISIBLE);

        imgTiempo.setVisibility(View.INVISIBLE);
        imgTiempo.setVisibility(View.INVISIBLE);
        imgTiempo.setVisibility(View.INVISIBLE);
        imgAmanecer.setVisibility(View.INVISIBLE);
        imgAnochecer.setVisibility(View.INVISIBLE);
        imgTempMax.setVisibility(View.INVISIBLE);
        imgTempMin.setVisibility(View.INVISIBLE);
        imgHumedad.setVisibility(View.INVISIBLE);

    }
}