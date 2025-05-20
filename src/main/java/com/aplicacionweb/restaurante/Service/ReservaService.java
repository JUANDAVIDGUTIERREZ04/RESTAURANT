package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Reservas.Reserva;
import com.aplicacionweb.restaurante.Repository.ReservaRepository;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void guardarReserva(Reserva reserva) {
        reserva.calcularAnticipacion();
        reservaRepository.save(reserva);
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }


    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id); 
    }
     

    public List<Reserva> buscarReservaPorNombre(String nombre) {
        return reservaRepository.findByNombre(nombre); // Llama al repositorio para buscar por nombre
    }
    


    // Nuevo método para actualizar el estado de una reserva
    public void actualizarEstadoReserva(Long id, String nuevoEstado) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();
            reserva.setEstadoReserva(nuevoEstado); // Actualiza el estado de la reserva
            reservaRepository.save(reserva); // Guarda los cambios
        }
    }

     public void actualizarReserva(Long id, Reserva reservaActualizada) {
    Optional<Reserva> reservaOptional = reservaRepository.findById(id);
    if (reservaOptional.isPresent()) {
        Reserva reservaExistente = reservaOptional.get();
        reservaExistente.setNombre(reservaActualizada.getNombre());
        reservaExistente.setTelefono(reservaActualizada.getTelefono());
        reservaExistente.setEmail(reservaActualizada.getEmail());
        reservaExistente.setFecha(reservaActualizada.getFecha());
        reservaExistente.setHoraInicio(reservaActualizada.getHoraInicio());
        reservaExistente.setHoraFin(reservaActualizada.getHoraFin());
        reservaExistente.setNumeroPersonas(reservaActualizada.getNumeroPersonas());
        reservaExistente.setMotivo(reservaActualizada.getMotivo());
        reservaExistente.setRestricciones(reservaActualizada.getRestricciones());
        reservaExistente.setMesa(reservaActualizada.getMesa());
        reservaExistente.setPrecio(reservaActualizada.getPrecio());
        reservaExistente.setEstadoReserva(reservaActualizada.getEstadoReserva());

        reservaRepository.save(reservaExistente);
    } else {
        throw new ResolutionException("Reserva no encontrada con ID " + id);
    }
}

}