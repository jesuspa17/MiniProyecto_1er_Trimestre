package com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_cincodias;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Coord;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.Sys;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Jesús Pallares on 11/11/2015.
 */
public class City {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("population")
    @Expose
    private long population;
    @SerializedName("sys")
    @Expose
    private Sys sys;

    /**
     * No args constructor for use in serialization
     *
     */
    public City() {
    }

    /**
     *
     * @param coord
     * @param id
     * @param sys
     * @param name
     * @param population
     * @param country
     */
    public City(long id, String name, Coord coord, String country, long population, Sys sys) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.population = population;
        this.sys = sys;
    }

    /**
     *
     * @return
     * The id
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The coord
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     *
     * @param coord
     * The coord
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The population
     */
    public long getPopulation() {
        return population;
    }

    /**
     *
     * @param population
     * The population
     */
    public void setPopulation(long population) {
        this.population = population;
    }

    /**
     *
     * @return
     * The sys
     */
    public Sys getSys() {
        return sys;
    }

    /**
     *
     * @param sys
     * The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
