package com.aplicacionweb.restaurante.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacionweb.restaurante.Models.CarritoItem;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.Menu;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario(User usuario);

    Optional<CarritoItem> findByUsuarioAndMenu(User usuario, Menu menu);
    // Método para obtener los CarritoItems activos de un usuario
    List<CarritoItem> findByUsuarioAndActivoTrue(User usuario);  // Solo activos

    // Buscar un item por usuario y producto que esté inactivo
    Optional<CarritoItem> findByUsuarioAndMenuAndActivoFalse(User usuario, Menu menu);

    // Buscar un item por usuario y producto que esté activo
    Optional<CarritoItem> findByUsuarioAndMenuAndActivoTrue(User usuario, Menu menu);

   

    
}

