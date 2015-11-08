package com.dam.salesianostriana.proyecto.weatherdam.model_google_places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesús Pallares on 07/11/2015.
 */
public class Prediction {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("matched_substrings")
    @Expose
    private List<List<Long>> matchedSubstrings = new ArrayList<List<Long>>();
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("terms")
    @Expose
    private List<Term> terms = new ArrayList<Term>();
    @SerializedName("types")
    @Expose
    private List<String> types = new ArrayList<String>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Prediction() {
    }

    /**
     *
     * @param id
     * @param placeId
     * @param description
     * @param terms
     * @param types
     * @param matchedSubstrings
     * @param reference
     */
    public Prediction(String description, String id, List<List<Long>> matchedSubstrings, String placeId, String reference, List<Term> terms, List<String> types) {
        this.description = description;
        this.id = id;
        this.matchedSubstrings = matchedSubstrings;
        this.placeId = placeId;
        this.reference = reference;
        this.terms = terms;
        this.types = types;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The matchedSubstrings
     */
    public List<List<Long>> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    /**
     *
     * @param matchedSubstrings
     * The matched_substrings
     */
    public void setMatchedSubstrings(List<List<Long>> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    /**
     *
     * @return
     * The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     *
     * @param placeId
     * The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     *
     * @return
     * The reference
     */
    public String getReference() {
        return reference;
    }

    /**
     *
     * @param reference
     * The reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     *
     * @return
     * The terms
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     *
     * @param terms
     * The terms
     */
    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    /**
     *
     * @return
     * The types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     *
     * @param types
     * The types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }
}
