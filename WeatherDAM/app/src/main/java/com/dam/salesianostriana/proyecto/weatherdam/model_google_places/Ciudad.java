package com.dam.salesianostriana.proyecto.weatherdam.model_google_places;

import android.gesture.Prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesús Pallares on 07/11/2015.
 */


public class Ciudad {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = new ArrayList<Prediction>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public Ciudad() {
    }

    /**
     *
     * @param predictions
     * @param status
     */
    public Ciudad(List<Prediction> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }

    /**
     *
     * @return
     * The predictions
     */
    public List<Prediction> getPredictions() {
        return predictions;
    }

    /**
     *
     * @param predictions
     * The predictions
     */
    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}