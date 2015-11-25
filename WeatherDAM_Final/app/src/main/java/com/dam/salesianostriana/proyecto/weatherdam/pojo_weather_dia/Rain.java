package com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Jesús Pallares on 15/11/2015.
 */
public class Rain {

    @SerializedName("3h")
    @Expose
    private double _3h;

    /**
     *
     * @return
     * The _3h
     */
    public double get3h() {
        return _3h;
    }

    /**
     *
     * @param _3h
     * The 3h
     */
    public void set3h(double _3h) {
        this._3h = _3h;
    }

}
