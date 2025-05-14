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

    private int anticipacion; // Atributo NUMERIC
    private int numeroPersonas; // Atributo NUMERIC // Atributo nominal {WEB, TELEFONO, AGENCIA}
    private String metodoDePago; // Atributo nominal {EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA,
                                 // PAYPAL} // Atributo nominal {pagada, no_pagada}
    private Boolean clienteRecurrente; // Atributo nominal {true, false}
    private String diaSemana;
    // Atributo nominal {LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO}
    // Atributo nominal {true, false} (esto es lo que se predice)

    @Override
    public String toString() {
        return "ReservaDTO{" +
                "numeroPersonas=" + numeroPersonas +
                ", metodoDePago='" + metodoDePago + '\'' +
                ", clienteRecurrente=" + clienteRecurrente +
                ", anticipacion=" + anticipacion +
                ", diaSemana='" + diaSemana + '\'' +
                '}';
    }

}
