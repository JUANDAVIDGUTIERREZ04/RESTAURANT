package com.aplicacionweb.restaurante.Repository;



import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.DetallePedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Método para buscar los detalles de pedido por CarritoItem
    List<DetallePedido> findByCarritoItem(CarritoItem carritoItem);

    // Aquí puedes agregar métodos personalizados si es necesario, por ejemplo:
    // List<DetallePedido> findByUsuario(User usuario);
}

