package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Reserva;
import com.aplicacionweb.restaurante.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "form_reserva"; // Devuelve la vista con el formulario
    }

    @PostMapping
    public String guardarReserva(@ModelAttribute Reserva reserva) {
        reservaService.guardarReserva(reserva);
        return "index"; // Redirige después de guardar
    }

    @GetMapping("/listaReserva")
    public String listaReserva(Model model) {
        // Obtener todas las reservas desde el servicio
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        // Añadir las reservas al modelo
        model.addAttribute("reservas", reservas);
        return "reserva_lista"; // Vista donde se mostrarán las reservas
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);  // Llamamos al servicio para eliminar la reserva
        return "redirect:/reservas/listaReserva";  // Redirigimos a la lista de reservas después de eliminar
    }
}
