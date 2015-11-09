package com.salesianostriana.ad.gsonlistviewsample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luismi on 02/11/2015.
 */
public class Country {


    private String name;
    @SerializedName("alpha-2")
    private String alpha;
    @SerializedName("country-code")
    private String countryCode;


    public Country() {}


    public Country(String alpha, String countryCode, String name) {
        this.alpha = alpha;
        this.countryCode = countryCode;
        this.name = name;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "alpha='" + alpha + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
