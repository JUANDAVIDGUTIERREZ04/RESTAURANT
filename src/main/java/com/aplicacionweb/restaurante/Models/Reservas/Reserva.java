package com.aplicacionweb.restaurante.Models.Reservas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

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
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime fecha; // Fecha del check-in

    @Column(nullable = false)
    private LocalDateTime fechaReserva; // Fecha en que se hizo la reserva

    @Column(nullable = false)
    private Integer numeroPersonas;

    private String motivo;
    private String restricciones;
    private String comentarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoDePago metodoDePago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenReserva origenReserva;

    @Column(nullable = false)
    private String estadoReserva = "no pagada"; // pagada / no pagada

    @Column(nullable = false)
    private Boolean clienteRecurrente = false;

    @Column(nullable = false)
    private Boolean cancelada = false;

    @Column(nullable = false)
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    /**
     * Calcula la anticipación en días entre la fecha de reserva y la fecha de check-in
     */
    public long getAnticipacion() {
        if (fechaReserva != null && fecha != null) {
            return Duration.between(fechaReserva, fecha).toDays();
        }
        return 0;
    }

    // Puedes agregar otros métodos auxiliares si los necesitas, como:
    // esFindeSemana(), esTemporadaAlta(), etc.
}
