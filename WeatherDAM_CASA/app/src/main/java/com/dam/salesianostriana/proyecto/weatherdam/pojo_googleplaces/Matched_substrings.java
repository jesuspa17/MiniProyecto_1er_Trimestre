package com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces;

/**
 * Created by Jesús Pallares on 09/11/2015.
 */
public class Matched_substrings {

    private String length;

    private String offset;

    public String getLength ()
    {
        return length;
    }

    public void setLength (String length)
    {
        this.length = length;
    }

    public String getOffset ()
    {
        return offset;
    }

    public void setOffset (String offset)
    {
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [length = "+length+", offset = "+offset+"]";
    }
}
