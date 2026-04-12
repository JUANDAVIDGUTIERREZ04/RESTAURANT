package com.aplicacionweb.restaurante.Repository;

import com.aplicacionweb.restaurante.Models.Calificacion;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByMenu(Menu menu);  // Encontrar todas las calificaciones de un plato

    Optional<Calificacion> findByUsuarioAndMenu(User usuario, Menu menu);

    // Promedio de estrellas por menú
    @Query("SELECT AVG(c.estrellas) FROM Calificacion c WHERE c.menu.id = :menuId")
    Double promedioPorMenu(Long menuId);

    // Evitar que un usuario califique dos veces el mismo plato
    Optional<Calificacion> findByMenuAndUsuario(Menu menu, User usuario);


}
