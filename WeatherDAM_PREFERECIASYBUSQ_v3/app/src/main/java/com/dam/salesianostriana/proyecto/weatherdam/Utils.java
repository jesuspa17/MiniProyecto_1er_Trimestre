package com.dam.salesianostriana.proyecto.weatherdam;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Luismi on 04/11/2015.
 */
public class Utils {

    public static List<String> listadoCiudadesFav= new ArrayList<String>();

    public static String KEY_GOOGLE(){
        return "AIzaSyCAfWAIEXkY9aNcEPhR6z0s2pFFiojQjqg";
    }

    public static String URL_GOOGLE_PLACES_BASE(){
        return "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
    }

    public static String URL_GOOGLE_PLACES_APPEND(){
        return "&types=(cities)&language=es_ES&key=";
    }

    public static String KEY_APIWEATHER(){

        return "3bcfcde9b7438aa7696f020ed75f5673";
    }


    public static String URL_BASE_REQUEST(){
        return "http://api.openweathermap.org/data/2.5/weather?q=";
    }

    public static String URL_BASE_APPEND(){
        return "&units=metric&appid="+Utils.KEY_APIWEATHER()+"&lang=es";
    }

    public static String URL_BASE_FIVEDAYS(){
        return "http://api.openweathermap.org/data/2.5/forecast?q=";
    }


    public static String URL_BASE_IMG_WEATHER(){
        return "http://openweathermap.org/img/w/";
    }

    public static String EXTENSION_IMG_WEATHER(){
        return ".png";
    }


     /*
        Método que devuelve el bufferedreader asociado a una URL.
     */
    public static BufferedReader Url2BufferedReader(String url) throws IOException {

        //return new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
        URL Url = new URL(url);
        InputStream is = Url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br;
    }


    /*
        Método que devuelve el contenido de una URL como una cadena de caracteres
     */

    public static String getStringContentFromUrl(String url) throws IOException {

        StringBuilder sb = new StringBuilder();

        BufferedReader br =  Url2BufferedReader(url);

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        return sb.toString();

    }

    /**
     * Devuelve el día de la semana según la fecha que se le pase.
     * @param fecha
     * @return
     */
    public static String getDiaSemana(String fecha) {
        String valor_dia = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = null;
        try {
            fechaActual = df.parse(fecha);
        } catch (ParseException e) {
            System.err.println("No se ha podido parsear la fecha.");
            e.printStackTrace();
        }
        GregorianCalendar fechaCalendario = new GregorianCalendar();
        fechaCalendario.setTime(fechaActual);
        int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK);

        if (diaSemana == 1) {
            valor_dia = "Domingo";
        } else if (diaSemana == 2) {
            valor_dia = "Lunes";
        } else if (diaSemana == 3) {
            valor_dia = "Martes";
        } else if (diaSemana == 4) {
            valor_dia = "Miercoles";
        } else if (diaSemana == 5) {
            valor_dia = "Jueves";
        } else if (diaSemana == 6) {
            valor_dia = "Viernes";
        } else if (diaSemana == 7) {
            valor_dia = "Sabado";
        }
        return valor_dia;
    }


    public static SharedPreferences sp;

    public static boolean guardarArray(Context mContext) {

        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("arrayCiudadesSize", listadoCiudadesFav.size()); /* sKey is an array */

        for(int i=0;i<listadoCiudadesFav.size();i++) {
            mEdit1.remove("Posicion_" + i);
            mEdit1.putString("Posicion_" + i, listadoCiudadesFav.get(i));
        }
        return mEdit1.commit();
    }

    public static void cargarArray(Context mContext){
        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        listadoCiudadesFav.clear();
        int size = sp.getInt("arrayCiudadesSize", 0);
        for(int i=0;i<size;i++)
        {
            listadoCiudadesFav.add(sp.getString("Posicion_" + i, null));
        }
    }



}
