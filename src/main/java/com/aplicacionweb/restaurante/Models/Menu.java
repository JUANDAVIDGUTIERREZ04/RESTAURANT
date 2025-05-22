package com.aplicacionweb.restaurante.Models;

import java.util.List;

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

    
    private Double calificacionPromedio;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Calificacion> calificaciones;

    public Menu orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    } 

    public double getCalificacionPromedioCalculado() {
    if (calificaciones == null || calificaciones.isEmpty()) {
        return 0;
    }

    double total = calificaciones.stream().mapToInt(Calificacion::getEstrellas).sum();
    double promedio = total / calificaciones.size();
    
    return Math.round(promedio * 2) / 2.0; // Redondeo a .0 o .5
}

}
