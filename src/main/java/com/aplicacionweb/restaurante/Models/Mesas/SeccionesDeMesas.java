package com.aplicacionweb.restaurante.Models.Mesas;

public enum SeccionesDeMesas {

    ZONA_A("Zona A", 50000),
    ZONA_B("Zona B", 100000),
    ZONA_C("Zona C", 150000);

    private String nombre;
    private int precio;

    // Constructor
    SeccionesDeMesas(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Métodos getter
    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }
}
