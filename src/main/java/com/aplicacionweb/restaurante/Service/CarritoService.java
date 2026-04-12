package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Repository.CarritoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    // Agregar item al carrito
    public void agregarItemAlCarrito(User usuario, Menu menu, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        Optional<CarritoItem> existente =
                carritoRepository.findByUsuarioAndMenuAndActivoTrue(usuario, menu);

        if (existente.isPresent()) {

            CarritoItem item = existente.get();
            item.setCantidad(cantidad);
            item.setSubtotal(calcularSubtotal(item));
            carritoRepository.save(item);

        } else {

            Optional<CarritoItem> inactivo =
                    carritoRepository.findByUsuarioAndMenuAndActivoFalse(usuario, menu);

            if (inactivo.isPresent()) {

                CarritoItem item = inactivo.get();
                item.setActivo(true);
                item.setCantidad(cantidad);
                item.setSubtotal(calcularSubtotal(item));
                carritoRepository.save(item);

            } else {

                CarritoItem item = new CarritoItem();
                item.setUsuario(usuario);
                item.setMenu(menu);
                item.setCantidad(cantidad);
                item.setSubtotal(calcularSubtotal(item));
                carritoRepository.save(item);
            }
        }
    }

    // 🔥 CORREGIDO: BigDecimal
    private BigDecimal calcularSubtotal(CarritoItem item) {

        if (item.getMenu() == null ||
                item.getMenu().getPrecio() == null ||
                item.getCantidad() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal precio = item.getMenu().getPrecio();
        BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());

        return precio.multiply(cantidad);
    }

    // Obtener carrito activo
    public List<CarritoItem> obtenerListaCarrito(User usuario) {
        return carritoRepository.findByUsuarioAndActivoTrue(usuario);
    }

    public void guardar(CarritoItem item) {
        carritoRepository.save(item);
    }

    // Eliminar (soft delete)
    public void eliminarProductoDelCarrito(Long itemId) {

        Optional<CarritoItem> carritoItem = carritoRepository.findById(itemId);

        if (carritoItem.isPresent()) {
            CarritoItem item = carritoItem.get();
            item.setActivo(false);
            carritoRepository.save(item);
        } else {
            throw new RuntimeException("Item no encontrado en el carrito.");
        }
    }
}