package com.aplicacionweb.restaurante.Models;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Prediccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numeroPersonas;
    private String origenReserva;
    private String metodoPago;
    private boolean clienteRecurrente;
    private double anticipacion;
    private String diaSemana;
    private String estadoReserva; 

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    @Column(updatable = false)
    private LocalDateTime fecha;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public String getOrigenReserva() {
        return origenReserva;
    }

    public void setOrigenReserva(String origenReserva) {
        this.origenReserva = origenReserva;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean isClienteRecurrente() {
        return clienteRecurrente;
    }

    public void setClienteRecurrente(boolean clienteRecurrente) {
        this.clienteRecurrente = clienteRecurrente;
    }

    public double getAnticipacion() {
        return anticipacion;
    }

    public void setAnticipacion(double anticipacion) {
        this.anticipacion = anticipacion;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }


    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    
}

