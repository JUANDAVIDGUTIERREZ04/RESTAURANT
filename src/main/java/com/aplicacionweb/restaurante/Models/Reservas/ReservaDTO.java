package com.aplicacionweb.restaurante.Models.Reservas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ReservaDTO {

    private Double anticipacion;        // Atributo NUMERIC
    private Integer numeroPersonas;     // Atributo NUMERIC
    private String origenReserva;       // Atributo nominal {WEB, TELEFONO, AGENCIA}
    private String metodoDePago;        // Atributo nominal {EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, PAYPAL}
    private String estadoReserva;       // Atributo nominal {pagada, no_pagada}
    private Boolean clienteRecurrente;  // Atributo nominal {true, false}
    private String diaSemana;           // Atributo nominal {LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO}
    private Boolean cancelada;          // Atributo nominal {true, false} (esto es lo que se predice)

}
