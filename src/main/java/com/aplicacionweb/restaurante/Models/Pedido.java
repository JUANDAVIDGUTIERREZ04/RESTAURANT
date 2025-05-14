package com.aplicacionweb.restaurante.Models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.aplicacionweb.restaurante.Models.Reservas.MetodoDePago;




@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(nullable = false)
    private String diaSemana;

    @Column(nullable = false)
    private String estado = "Pendiente"; // Valor predeterminado

   

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private TipoEntregaPedido tipoEntregaPedido;
    
     @Enumerated(EnumType.STRING)
    private MetodoDePago metodoDePago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Double total;

    @PrePersist
    protected void onCreate() {
        fecha = new Date();// Establece la fecha y hora actuales


        LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        diaSemana = localDate.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault());
    }

    // Método para establecer el precio según el tipo de entrega
    public void tipoEntrega() {
        if (this.tipoEntregaPedido != null) {
            switch (this.tipoEntregaPedido) {
                case DOMICILIO:
                    // Si el tipo de entrega es Domicilio, asignamos un precio adicional (por ejemplo, 5.0)
                    this.total = this.cantidad * this.menu.getPrecio() + 5000;
                    break;
                case PERSONALMENTE:
                    // Si el tipo de entrega es Personalmente, no hay costo adicional
                    this.total = this.cantidad * this.menu.getPrecio();
                    break;
                default:
                    this.total = this.cantidad * this.menu.getPrecio();
                    break;
            }
        }
    }

    }


     


