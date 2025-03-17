package com.aplicacionweb.restaurante.Repository;



import com.aplicacionweb.restaurante.Models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
}

