package com.aplicacionweb.restaurante.Service;



import com.aplicacionweb.restaurante.Models.Reserva;
import com.aplicacionweb.restaurante.Repository.ReservaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void guardarReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }
    
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);  // Elimina la reserva por su ID
    }
}
