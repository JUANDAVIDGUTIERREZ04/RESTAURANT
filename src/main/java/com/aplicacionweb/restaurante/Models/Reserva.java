package com.aplicacionweb.restaurante.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "reservas") // Asegúrate de que el nombre de la tabla sea correcto
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
    private LocalDateTime fecha; // Cambiado a Date para manejar fecha y hora

    @Column(nullable = false)
    private Integer numeroPersonas;

    private String motivo;  // Ejemplo: "confirmada", "cancelada"
    private String restricciones;
    private String comentarios;
}
