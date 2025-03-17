package com.aplicacionweb.restaurante.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreDePlato;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    // Cambiado de byte[] a String para almacenar la URL de la imagen
    @Column(length = 255)
    private String imagenUrl;  // Para almacenar la URL de la imagen
    
    // Nueva propiedad categoria
    @Column(length = 100) // Puedes ajustar la longitud según sea necesario
    private String categoria;  // Para almacenar la categoría del menú

    @Column(nullable = true)
    private Double calificacionPromedio; 
}
