package com.aplicacionweb.restaurante.Service;



import com.aplicacionweb.restaurante.Models.DetallePedido;
import com.aplicacionweb.restaurante.Repository.DetallePedidoRepository;

import jakarta.transaction.Transactional;

import com.aplicacionweb.restaurante.Models.CarritoItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    DetallePedidoRepository detallePedidoRepository;


    // Método para obtener los detalles de pedido de un usuario
    public List<DetallePedido> obtenerDetallesPorUsuario(Long userId) {
        return detallePedidoRepository.findByCarritoItem_Usuario_Id(userId);
    }

    
    


    // Método para guardar los detalles del pedido
    @Transactional
    public void guardarDetallesPedido(List<DetallePedido> detallesPedido) {
        detallePedidoRepository.saveAll(detallesPedido);  // Guardar todos los detalles del pedido en la base de datos
    }

    // Método para crear detalles del pedido a partir de los items del carrito
    public List<DetallePedido> crearDetallePedido(List<CarritoItem> itemsCarrito, String tipoEntrega) {
        List<DetallePedido> detallesPedido = new ArrayList<>();
        for (CarritoItem item : itemsCarrito) {
            DetallePedido detalle = new DetallePedido();
            detalle.setCarritoItem(item);
            detalle.setFechaHora(LocalDateTime.now());
            detalle.setDiaSemana(detalle.getFechaHora().getDayOfWeek().toString());
            detalle.setTipoEntrega(tipoEntrega);

            detallesPedido.add(detalle);
        }

        return detallesPedido;
    }

    // Método para desvincular los detalles de los CarritoItem
    public void desvincularDetallesDelCarrito(List<CarritoItem> carritoItems) {
        for (CarritoItem item : carritoItems) {
            // Buscar los detalles de pedido asociados a cada CarritoItem
            List<DetallePedido> detalles = detallePedidoRepository.findByCarritoItem(item);
            for (DetallePedido detalle : detalles) {
                detalle.setCarritoItem(null);  // Desvinculamos el detalle del carrito
                detallePedidoRepository.save(detalle);  // Guardamos el detalle desvinculado
            }
        }
    }
}
