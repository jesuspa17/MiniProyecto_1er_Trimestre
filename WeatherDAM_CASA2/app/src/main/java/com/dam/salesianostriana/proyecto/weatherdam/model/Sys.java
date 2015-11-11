
package com.dam.salesianostriana.proyecto.weatherdam.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class Sys {

    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Date sunrise;
    @SerializedName("sunset")
    @Expose
    private Date sunset;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sys() {
    }

    /**
     * 
     * @param message
     * @param sunset
     * @param sunrise
     * @param country
     */
    public Sys(double message, String country, Date sunrise, Date sunset) {
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    /**
     * 
     * @return
     *     The message
     */
    public double getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(double message) {
        this.message = message;
    }

    public Sys withMessage(double message) {
        this.message = message;
        return this;
    }

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public Sys withCountry(String country) {
        this.country = country;
        return this;
    }

    /**
     * 
     * @return
     *     The sunrise
     */
    public Date getSunrise() {
        return sunrise;
    }

    /**
     * 
     * @param sunrise
     *     The sunrise
     */
    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    public Sys withSunrise(Date sunrise) {
        this.sunrise = sunrise;
        return this;
    }

    /**
     * 
     * @return
     *     The sunset
     */
    public Date getSunset() {
        return sunset;
    }

    /**
     * 
     * @param sunset
     *     The sunset
     */
    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }

    public Sys withSunset(Date sunset) {
        this.sunset = sunset;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
