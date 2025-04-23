package com.aplicacionweb.restaurante.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aplicacionweb.restaurante.Models.Reservas.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {

    List<Reserva> findByNombre(String nombre);
    
}