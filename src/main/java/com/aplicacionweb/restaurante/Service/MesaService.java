package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Mesas.*;
import com.aplicacionweb.restaurante.Repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    // Obtener todas las mesas disponibles
    public List<Mesa> obtenerMesasDisponibles() {
        return mesaRepository.findByDisponibleTrue();
    }

    // Obtener una mesa por su ID
    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    // Cambiar el estado de disponibilidad de una mesa
    public void cambiarDisponibilidad(Long id, boolean disponible) {
        // Obtener la mesa por id
        Optional<Mesa> mesaOptional = mesaRepository.findById(id);
        
        if (mesaOptional.isPresent()) {
            Mesa mesa = mesaOptional.get();
            // Cambiar la disponibilidad de la mesa
            mesa.setDisponible(disponible);
            // Guardar la mesa actualizada en la base de datos
            mesaRepository.save(mesa);
        } else {
            // Si no se encuentra la mesa, lanzar excepción o manejar el error de forma adecuada
            throw new NoSuchElementException("Mesa no encontrada");
        }
    }
    

    // Guardar una nueva mesa
    public Mesa guardarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    // Eliminar una mesa
    public void eliminarMesa(Long id) {
        mesaRepository.deleteById(id);
    }
}
