package com.aplicacionweb.restaurante.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aplicacionweb.restaurante.Models.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    
}
