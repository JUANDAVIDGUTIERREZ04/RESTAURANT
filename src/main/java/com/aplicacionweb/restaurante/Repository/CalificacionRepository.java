package com.aplicacionweb.restaurante.Repository;

import com.aplicacionweb.restaurante.Models.Calificacion;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByMenu(Menu menu);  // Encontrar todas las calificaciones de un plato

    Optional<Calificacion> findByUsuarioAndMenu(User usuario, Menu menu);

}
