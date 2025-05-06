package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Repository.CarritoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    // Método para agregar un item al carrito
    public void agregarItemAlCarrito(User usuario, Menu menu, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        // Buscar si el item ya existe en el carrito
        Optional<CarritoItem> existente = carritoRepository.findByUsuarioAndMenu(usuario, menu);

        if (existente.isPresent()) {
            // Si el item ya existe, actualizar la cantidad y subtotal
            CarritoItem item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.setSubtotal(calcularSubtotal(item));
            carritoRepository.save(item);
        } else {
            // Si el item no existe, agregarlo como un nuevo item
            CarritoItem item = new CarritoItem();
            item.setUsuario(usuario);
            item.setMenu(menu);
            item.setCantidad(cantidad);
            item.setSubtotal(calcularSubtotal(item));
            carritoRepository.save(item);
        }
    }

    // Método auxiliar para calcular el subtotal
    private double calcularSubtotal(CarritoItem item) {
        return item.getMenu().getPrecio() * item.getCantidad();
    }

    // Obtener los elementos del carrito de un usuario
    public List<CarritoItem> obtenerListaCarrito(User usuario) {
        return carritoRepository.findByUsuarioAndActivoTrue(usuario);  // Solo ítems activos
    }

    // Método para guardar un CarritoItem (por si lo necesitas)
    public void guardar(CarritoItem item) {
        carritoRepository.save(item);
    }

    // Método para eliminar un CarritoItem del carrito
    public void eliminarProductoDelCarrito(Long itemId) {
        // Buscar el CarritoItem por su ID
        Optional<CarritoItem> carritoItem = carritoRepository.findById(itemId);

        if (carritoItem.isPresent()) {
            // Marcar el CarritoItem como inactivo (en lugar de eliminarlo)
            CarritoItem item = carritoItem.get();
            item.setActivo(false);  // Marcar como inactivo, no lo eliminamos de la base de datos
            carritoRepository.save(item);  // Guardar el estado actualizado
        } else {
            throw new RuntimeException("Item no encontrado en el carrito.");
        }
    }
}
