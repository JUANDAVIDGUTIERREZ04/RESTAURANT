package com.aplicacionweb.restaurante.Models.Mesas;

import java.util.List;

import com.aplicacionweb.restaurante.Models.Reservas.Reserva;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mesas") // Asegúrate de que el nombre de la tabla esté en minúsculas
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    @Column(nullable = false, unique = true)
    private Integer numeroMesa;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private Boolean disponible;

    @Enumerated(EnumType.STRING) // Para almacenar el valor del enum en la base de datos como una cadena.
    @Column(nullable = false)
    private SeccionesDeMesas seccion; // Sección de la mesa

    @Column(nullable = false) // Guardamos el precio en la base de datos
    private Double precio; // Precio de la sección de la mesa

    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas;

    // Método para establecer el precio según la sección seleccionada
    public void setPrecio() {
        if (this.seccion != null) {
            System.out.println("Sección seleccionada: " + this.seccion); // Verifica el valor de seccion
            this.precio = (double) this.seccion.getPrecio();
        } else {
            System.out.println("Error: La sección es nula."); // Verifica si seccion es null
        }
    }

}
