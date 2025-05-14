package com.aplicacionweb.restaurante.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;  // Relación con el menú

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;  // Relación con el usuario que califica

    @Column(nullable = false)
    private Integer estrellas;  // Valor de la calificación (1 a 5)
}
