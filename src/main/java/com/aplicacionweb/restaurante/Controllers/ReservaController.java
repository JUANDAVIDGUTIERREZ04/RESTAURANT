package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Mesas.Mesa;
import com.aplicacionweb.restaurante.Models.Reservas.Reserva;
import com.aplicacionweb.restaurante.Repository.MesaRepository;
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

    @Autowired

    private MesaRepository mesaRepository;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("mesas", mesaService.obtenerMesasDisponibles()); // Asumiendo que tienes un servicio para
                                                                            // obtener las mesas disponibles
        return "form_reserva"; // Vista del formulario
    }

    @PostMapping("/registrar")
    public String guardarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el objeto reserva tiene mesa
            if (reserva.getMesa() != null && reserva.getMesa().getIdMesa() != null) {
                Mesa mesaSeleccionada = mesaRepository.findById(reserva.getMesa().getIdMesa()).orElse(null);

                if (mesaSeleccionada != null) {
                    // Asignar la mesa seleccionada a la reserva
                    reserva.setMesa(mesaSeleccionada);

                    // Asignar el precio de la mesa a la reserva
                    reserva.setPrecio(reserva.getMesa().getPrecio());

                    // Si es necesario, también puedes llamar a setPrecio para ajustar el precio en
                    // caso de algún cambio
                    mesaSeleccionada.setPrecio();
                } else {
                    // En caso de que la mesa no se encuentre
                    redirectAttributes.addFlashAttribute("errorReserva", "Mesa no encontrada.");
                    return "redirect:/reservas";
                }
            }

            // Si la reserva no tiene estado, se asigna "no pagada" por defecto
            if (reserva.getEstadoReserva() == null || reserva.getEstadoReserva().isEmpty()) {
                reserva.setEstadoReserva("no pagada");
            }

            // Guardar la reserva
            reservaService.guardarReserva(reserva);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("registroReserva", "¡Reserva realizada con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorReserva",
                    "Hubo un problema al realizar la reserva. Intente nuevamente.");
            e.printStackTrace(); // Imprimir el error para depuración
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
        return "redirect:/reservas/listaReserva"; // Redirigir a la lista
    }

    // método para cambiar el estado de la reserva
    @PostMapping("/actualizarEstado/{id}")
    public String actualizarEstadoReserva(@PathVariable Long id, @RequestParam String estado,
            RedirectAttributes redirectAttributes) {
        try {
            reservaService.actualizarEstadoReserva(id, estado); // Cambia el estado de la reserva
            redirectAttributes.addFlashAttribute("estadoActualizado", "Estado actualizado a: " + estado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorEstado", "Hubo un error al actualizar el estado.");
        }
        return "redirect:/reservas/listaReserva"; // Redirigir a la lista de reservas
    }

    //  buscar reservas por nombre
    @GetMapping("/buscar")
    public String buscarReservaPorNombre(@RequestParam("nombre") String nombre, Model model) {
        List<Reserva> reservas = reservaService.buscarReservaPorNombre(nombre);
        
        // Añadir las reservas al modelo para que Thymeleaf las pueda mostrar
        model.addAttribute("reservas", reservas);

        // Si no se encuentran reservas, podemos agregar un mensaje adicional al modelo
        if (reservas.isEmpty()) {
            model.addAttribute("mensaje", "No se encontraron reservas con ese nombre.");
        }

        // Devuelve la vista que se renderiza
        return "reserva_lista"; // La plantilla HTML que mostrará los resultados
    }
}
