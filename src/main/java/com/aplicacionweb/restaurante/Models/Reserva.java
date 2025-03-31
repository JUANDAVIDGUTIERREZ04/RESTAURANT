package com.aplicacionweb.restaurante.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Integer numeroPersonas;

    private String motivo;
    private String restricciones;
    private String comentarios;

     // Relación Many-to-One entre Reserva y Mesa
     @ManyToOne
     @JoinColumn(name = "mesa_id") // Foreign key a Mesa
     private Mesa mesa;
}
