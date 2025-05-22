package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.DetallePedido;
import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Método para obtener los detalles de pedido de un usuario
    public List<DetallePedido> obtenerDetallesPorUsuario(Long userId) {
        return detallePedidoRepository.findByCarritoItem_Usuario_Id(userId);
    }

    // Método para guardar los detalles del pedido
    @Transactional
    public void guardarDetallesPedido(List<DetallePedido> detallesPedido) {
        detallePedidoRepository.saveAll(detallesPedido);  // Guardar todos los detalles del pedido
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

    // Método para desvincular los detalles de los CarritoItems (sin eliminar los Detalles)
    public void desvincularDetallesDelCarrito(CarritoItem carritoItem) {
        // Buscar los detalles de pedido asociados a un CarritoItem
        List<DetallePedido> detalles = detallePedidoRepository.findByCarritoItem(carritoItem);
        for (DetallePedido detalle : detalles) {
            detalle.setCarritoItem(null);  // Desvincula el detalle del CarritoItem
            detallePedidoRepository.save(detalle);  // Guarda el detalle desvinculado
        }
    }

    public Page<DetallePedido> obtenerPedidosPaginados(Pageable pageable) {
    return detallePedidoRepository.findAll(pageable);
}


     public void eliminarDetallePedidoPorId(Long id) {
        if (detallePedidoRepository.existsById(id)) {  // Verifica si el detalle de pedido existe
            detallePedidoRepository.deleteById(id);  // Elimina el detalle de pedido por su ID
        } else {
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + id);  // Lanza excepción si no existe
        }
    }
}
