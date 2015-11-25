package com.dam.salesianostriana.proyecto.weatherdam.pojo_listas;

/**
 * Created by Jesús Pallares on 11/11/2015.
 */
public class ItemFavorito {

    private String imagen;
    private String nombre;
    private String temperatura;

    public ItemFavorito(String nombre) {
        this.nombre = nombre;
    }

    public ItemFavorito(String nombre, String temperatura) {
        this.nombre = nombre;
        this.temperatura = temperatura;
    }

    public ItemFavorito(String imagen, String nombre, String temperatura) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.temperatura = temperatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
}