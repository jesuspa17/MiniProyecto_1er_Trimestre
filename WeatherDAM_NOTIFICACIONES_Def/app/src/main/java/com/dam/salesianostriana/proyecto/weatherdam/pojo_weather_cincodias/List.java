package com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_cincodias;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Clouds;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Main;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Weather;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Wind;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;

/**
 * @author Jesús Pallares on 11/11/2015.
 */
public class List {
    @SerializedName("dt")
    @Expose
    private long dt;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("sys")
    @Expose
    private Sys_ sys;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    /**
     * No args constructor for use in serialization
     *
     */
    public List() {
    }

    /**
     *
     * @param clouds
     * @param dt
     * @param wind
     * @param sys
     * @param dtTxt
     * @param weather
     * @param main
     */
    public List(long dt, Main main, java.util.List<Weather> weather, Clouds clouds, Wind wind, Sys_ sys, String dtTxt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.sys = sys;
        this.dtTxt = dtTxt;
    }

    /**
     *
     * @return
     * The dt
     */
    public long getDt() {
        return dt;
    }

    /**
     *
     * @param dt
     * The dt
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     *
     * @return
     * The main
     */
    public Main getMain() {
        return main;
    }

    /**
     *
     * @param main
     * The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @return
     * The weather
     */
    public java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     *
     * @param clouds
     * The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     *
     * @return
     * The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     *
     * @param wind
     * The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     *
     * @return
     * The sys
     */
    public Sys_ getSys() {
        return sys;
    }

    /**
     *
     * @param sys
     * The sys
     */
    public void setSys(Sys_ sys) {
        this.sys = sys;
    }

    /**
     *
     * @return
     * The dtTxt
     */
    public String getDtTxt() {
        return dtTxt;
    }

    /**
     *
     * @param dtTxt
     * The dt_txt
     */
    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
