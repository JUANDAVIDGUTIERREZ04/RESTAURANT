package com.aplicacionweb.restaurante.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.CarritoRepository;

@Service
public class CarritoService {

    


    @Autowired
    private CarritoRepository carritoRepository;

    public void agregarItemAlCarrito(User usuario, Menu menu, int cantidad) {
        // Ver si ya existe un ítem del mismo menú en el carrito del usuario
        Optional<CarritoItem> existente = carritoRepository.findByUsuarioAndMenu(usuario, menu);

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.setSubtotal(item.getMenu().getPrecio() * item.getCantidad());
            carritoRepository.save(item);
        } else {
            CarritoItem item = new CarritoItem();
            item.setUsuario(usuario);
            item.setMenu(menu);
            item.setCantidad(cantidad);
            item.setSubtotal(menu.getPrecio() * cantidad);
            carritoRepository.save(item);
        }
    }


    // Obtener los elementos del carrito de un usuario
    public List<CarritoItem> obtenerListaCarrito(User usuario) {
        return carritoRepository.findByUsuario(usuario); // Esto asume que ya tienes un método en el repositorio
    }

    public void guardar(CarritoItem item) {
        carritoRepository.save(item);
    }

    // Método para limpiar el carrito del usuario
    
    // Método para eliminar un CarritoItem (marcarlo como inactivo)
    public void eliminarCarritoItem(Long itemId) {
        // Buscar el CarritoItem por su ID
        CarritoItem item = carritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        // Marcarlo como inactivo
        item.setActivo(false);

        // Guardar el CarritoItem con el campo "activo" actualizado
        carritoRepository.save(item);
    }
    
    
    
    
}

