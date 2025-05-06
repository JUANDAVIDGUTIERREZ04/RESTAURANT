package com.aplicacionweb.restaurante.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacionweb.restaurante.Models.Prediccion;

public interface PrediccionRepository extends JpaRepository<Prediccion, Long> {
}
