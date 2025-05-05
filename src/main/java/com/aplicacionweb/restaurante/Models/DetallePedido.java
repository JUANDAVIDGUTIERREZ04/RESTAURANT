package com.aplicacionweb.restaurante.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aplicacionweb.restaurante.Models.Reservas.MetodoDePago;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)  // Cambié CascadeType.PERSIST por CascadeType.MERGE
    private CarritoItem carritoItem;

    private LocalDateTime fechaHora;
    private String diaSemana;
    private String tipoEntrega;

    private String direccion;

    @Enumerated(EnumType.STRING)
    private MetodoDePago metodoDePago;

    public DetallePedido(CarritoItem carritoItem, String tipoEntrega) {
        this.carritoItem = carritoItem;
        this.fechaHora = LocalDateTime.now();
        this.diaSemana = fechaHora.getDayOfWeek().toString();
        this.tipoEntrega = tipoEntrega;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
        if (this.diaSemana == null) {
            this.diaSemana = fechaHora.getDayOfWeek().toString();
        }
    }

    @PreUpdate
    public void preUpdate() {
        // Aquí podrías poner alguna lógica adicional antes de actualizar si es necesario
    }

    public String getFechaHoraFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }

    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaHora.toLocalDate().format(formatter);
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", carritoItem=" + carritoItem +
                ", fechaHora=" + fechaHora +
                ", diaSemana='" + diaSemana + '\'' +
                ", tipoEntrega='" + tipoEntrega + '\'' +
                '}';
    }
}
