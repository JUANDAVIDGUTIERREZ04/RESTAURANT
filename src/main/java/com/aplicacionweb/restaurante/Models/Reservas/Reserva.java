package com.aplicacionweb.restaurante.Models.Reservas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

import java.time.LocalTime;

import com.aplicacionweb.restaurante.Models.Mesas.Mesa;

@Entity
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reserva {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fechaReserva; // Fecha en que se hizo la reserva

    @Column(nullable = false , name = "check-in")
    private LocalDate fecha; // Fecha del check-in

    @Column(nullable = false)
    private LocalTime horaInicio; // Hora en que comienza la reserva

    @Column(nullable = false)
    private LocalTime horaFin; // Hora en que termina la reserva

    @Column(nullable = false)
    private int numeroPersonas;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String estadoReserva = "no pagada"; // pagada / no pagada

    private String motivo
    ;
    private String restricciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoDePago metodoDePago;

     @Column(nullable = false)
    private Boolean cancelada = false;

     @Column(nullable = false)
    private Boolean clienteRecurrente = false;


    @Column(nullable = false)
    private Long anticipacion; // Columna para almacenar la anticipación en días

    @Column(nullable = true)
    private String probabilidadCancelacion; // Enlace a la página con la probabilidad de cancelación

    /**
     * Calcula la anticipación en días entre la fecha de reserva y la fecha de
     * check-in
     */
    public void calcularAnticipacion() {
    if (fechaReserva != null && fecha != null) {
        this.anticipacion = Duration.between(fechaReserva.atStartOfDay(), fecha.atStartOfDay()).toDays();
    } else {
        this.anticipacion = 0L;
    }
}



    // Método para actualizar el enlace de probabilidad de cancelación
    public void actualizarProbabilidadCancelacion(String enlaceProbabilidad) {
        this.probabilidadCancelacion = enlaceProbabilidad;
    }

    public void setDiaSemana(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDiaSemana'");
    }

    // Puedes agregar otros métodos auxiliares si los necesitas, como:
    // esFindeSemana(), esTemporadaAlta(), etc.
}
