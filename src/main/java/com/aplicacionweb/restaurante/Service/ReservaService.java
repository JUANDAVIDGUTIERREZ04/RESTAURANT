package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Reservas.Reserva;
import com.aplicacionweb.restaurante.Repository.ReservaRepository;

import java.lang.module.ResolutionException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Reserva> obtenerReservasPaginadas(int page, int pageSize) {
    PageRequest pageable = PageRequest.of(page, pageSize); // Crea un Pageable con la página y el tamaño
    return reservaRepository.findAll(pageable); // Devuelve la página de reservas
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

public boolean existeConflictoDeReserva(Reserva nuevaReserva) {
    List<Reserva> reservasExistentes = reservaRepository.findByMesaIdMesaAndFecha(
            nuevaReserva.getMesa().getIdMesa(),
            nuevaReserva.getFecha()
    );

    for (Reserva r : reservasExistentes) {
        if (!r.getCancelada() && seCruzan(r.getHoraInicio(), r.getHoraFin(), nuevaReserva.getHoraInicio(), nuevaReserva.getHoraFin())) {
            return true;
        }
    }
    return false;
}

private boolean seCruzan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
    return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
}

}