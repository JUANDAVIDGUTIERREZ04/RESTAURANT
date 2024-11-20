package com.aplicacionweb.restaurante.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import com.aplicacionweb.restaurante.Models.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa,Long> {
    
}
