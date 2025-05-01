package com.aplicacionweb.restaurante.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Menu menu;

    private Integer cantidad;

    

    private Double subtotal;


    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (menu != null && cantidad != null) {
            this.subtotal = menu.getPrecio() * cantidad;
        }
    }
}

