package com.aplicacionweb.restaurante.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import com.aplicacionweb.restaurante.Models.Mesas.*;;

@Repository
public interface MesaRepository extends JpaRepository<Mesa,Long> {
    List<Mesa> findByDisponibleTrue();

    @Query("SELECT m FROM Mesa m LEFT JOIN FETCH m.reservas")
List<Mesa> findAllWithReservas();

    
}
