
package com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Clouds {

    @SerializedName("all")
    @Expose
    private int all;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Clouds() {
    }

    /**
     * 
     * @param all
     */
    public Clouds(int all) {
        this.all = all;
    }

    /**
     * 
     * @return
     *     The all
     */
    public int getAll() {
        return all;
    }

    /**
     * 
     * @param all
     *     The all
     */
    public void setAll(int all) {
        this.all = all;
    }

    public Clouds withAll(int all) {
        this.all = all;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
