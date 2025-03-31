package com.aplicacionweb.restaurante.Models;

public enum TipoEntregaPedido {

    DOMICILIO("Domicilio"),
    PERSONALMENTE("Personalmente");

    private String tipoEntrega;

    // Constructor
    TipoEntregaPedido(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    // Método getter
    public String getTipoEntrega() {
        return tipoEntrega;
    }
}
