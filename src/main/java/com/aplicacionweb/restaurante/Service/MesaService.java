package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Mesas.*;
import com.aplicacionweb.restaurante.Repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    // Obtener todas las mesas disponibles
    public List<Mesa> todasLasMesas() {
        return mesaRepository.findAll();
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

    public List<Mesa> obtenerTodasLasMesasConDisponibilidadActual() {
    List<Mesa> mesas = mesaRepository.findAllWithReservas(); // Usar JOIN FETCH aquí (ver más abajo)
    LocalDate hoy = LocalDate.now();
    LocalTime ahora = LocalTime.now();

    for (Mesa mesa : mesas) {
        boolean hayReservaActiva = false;

        if (mesa.getReservas() != null) {
            hayReservaActiva = mesa.getReservas().stream().anyMatch(reserva ->
                !reserva.getCancelada() &&
                reserva.getFecha().equals(hoy) &&
                ahora.isAfter(reserva.getHoraInicio()) &&
                ahora.isBefore(reserva.getHoraFin())
            );
        }

        // Mostrar ocupación en otra propiedad (sin modificar 'disponible' original)
        mesa.setDisponible(mesa.getDisponible() && !hayReservaActiva); 
        // Disponible solo si está marcada como disponible y no tiene reservas activas
    }

    return mesas;
}


}
