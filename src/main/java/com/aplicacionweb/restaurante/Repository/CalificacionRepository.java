package com.aplicacionweb.restaurante.Repository;

import com.aplicacionweb.restaurante.Models.Calificacion;
import com.aplicacionweb.restaurante.Models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByMenu(Menu menu);  // Encontrar todas las calificaciones de un plato
}
