package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Reserva;
import com.aplicacionweb.restaurante.Models.Mesas.Mesa;
import com.aplicacionweb.restaurante.Service.MesaService;
import com.aplicacionweb.restaurante.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private MesaService mesaService;

    @GetMapping
public String mostrarFormulario(Model model) {
    model.addAttribute("reserva", new Reserva());
    model.addAttribute("mesas", mesaService.obtenerMesasDisponibles());  // Asumiendo que tienes un servicio para obtener las mesas disponibles
    return "form_reserva"; // Vista del formulario
}


    @PostMapping("/registrar")
    public String guardarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        try {
            // Guardar la reserva en la base de datos
            reservaService.guardarReserva(reserva);

            // Mensaje de éxito
            String mensaje = "¡Reserva realizada con éxito!";
            redirectAttributes.addFlashAttribute("registroReserva", mensaje);
        } catch (Exception e) {
            String errorMensaje = "Hubo un problema al realizar la reserva. Intente nuevamente.";
            redirectAttributes.addFlashAttribute("errorReserva", errorMensaje);
        }

        return "redirect:/reservas"; // Redirigir de nuevo al formulario
    }

    @GetMapping("/listaReserva")
    public String listaReserva(Model model) {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        List<Mesa> mesas = mesaService.obtenerMesasDisponibles();
        model.addAttribute("reservas", reservas);
        model.addAttribute("mesas", mesas);
        return "reserva_lista"; // Vista para mostrar la lista de reservas
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return "redirect:/reservas/listaReserva";  // Redirigir a la lista
    }
}
