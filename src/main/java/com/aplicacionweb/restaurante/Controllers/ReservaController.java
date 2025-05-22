package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Mesas.Mesa;
import com.aplicacionweb.restaurante.Models.Reservas.Reserva;
import com.aplicacionweb.restaurante.Repository.MesaRepository;

import com.aplicacionweb.restaurante.Service.MesaService;
import com.aplicacionweb.restaurante.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
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

   

   @GetMapping("/listaReserva")
public String listarReservas(@RequestParam(defaultValue = "0") int page, Model model) {
    int pageSize = 5; // Establece el tamaño de la página
    // Obtener las reservas paginadas desde el servicio
    Page<Reserva> reservasPage = reservaService.obtenerReservasPaginadas(page, pageSize);

    // Obtener las mesas disponibles desde el servicio
    List<Mesa> mesas = mesaService.obtenerMesasDisponibles();  // Aquí se agregan las mesas disponibles

    // Pasar los datos a la vista
    model.addAttribute("reservas", reservasPage.getContent());
    model.addAttribute("totalPages", reservasPage.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("mesas", mesas); // Atributo de mesas

    return "reserva_lista"; // Nombre de la vista
}

@GetMapping("/formReservas")
    public String mostrarReservas() {
        return "form_reserva";
    }


   @PostMapping("/registrar")
public String guardarReserva(
        @ModelAttribute Reserva reserva,
        @RequestParam("rangoHorario") String rangoHorario,
        RedirectAttributes redirectAttributes) {
    try {
        // Procesar el rango de horario y asignarlo a horaInicio y horaFin
        String[] partesHorario = rangoHorario.split("-");
        if (partesHorario.length == 2) {
            // Convertir las cadenas de texto a LocalTime
            reserva.setHoraInicio(LocalTime.parse(partesHorario[0]));
            reserva.setHoraFin(LocalTime.parse(partesHorario[1]));
        } else {
            redirectAttributes.addFlashAttribute("errorReserva", "Horario inválido.");
            return "redirect:/reservas";
        }

        // Verificar si el objeto reserva tiene mesa
        if (reserva.getMesa() != null && reserva.getMesa().getIdMesa() != null) {
            Mesa mesaSeleccionada = mesaRepository.findById(reserva.getMesa().getIdMesa()).orElse(null);

            if (mesaSeleccionada != null) {
                // Asignar la mesa seleccionada a la reserva
                reserva.setMesa(mesaSeleccionada);

                // Asignar el precio de la mesa a la reserva
                reserva.setPrecio(reserva.getMesa().getPrecio());
            } else {
                // En caso de que la mesa no se encuentre
                redirectAttributes.addFlashAttribute("errorReserva", "Mesa no encontrada.");
                return "redirect:/reservas";
            }
        }

        // ⚠️ Validar conflicto de horario
        if (reservaService.existeConflictoDeReserva(reserva)) {
            redirectAttributes.addFlashAttribute("errorReserva", "Ya existe una reserva para esa mesa en ese horario.");
            return "redirect:/reservas";
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
        redirectAttributes.addFlashAttribute("errorReserva", "Hubo un problema al realizar la reserva. Intenta nuevamente.");
        e.printStackTrace(); // Imprimir el error para depuración
    }

    return "redirect:/reservas"; // Redirigir de nuevo al formulario
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

    // buscar reservas por nombre
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

   

    // Acción para manejar la actualización de una reserva
    @PostMapping("/actualizar/{id}")
    public String actualizarReserva(@PathVariable Long id, 
                                    @ModelAttribute Reserva reserva,
                                    @RequestParam Long mesaId) {
        // Buscar la reserva por ID
        Reserva reservaExistente = reservaService.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Actualizar los detalles de la reserva
        reservaExistente.setNombre(reserva.getNombre());
        reservaExistente.setTelefono(reserva.getTelefono());
        reservaExistente.setEmail(reserva.getEmail());
        reservaExistente.setHoraInicio(reserva.getHoraInicio());
        reservaExistente.setHoraFin(reserva.getHoraFin());
        reservaExistente.setNumeroPersonas(reserva.getNumeroPersonas());
        reservaExistente.setMotivo(reserva.getMotivo());
        reservaExistente.setRestricciones(reserva.getRestricciones());
        reservaExistente.setPrecio(reserva.getPrecio());
        reservaExistente.setEstadoReserva(reserva.getEstadoReserva());

        // Actualizar la mesa asociada
        Mesa mesa = mesaRepository.findById(mesaId).orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        reservaExistente.setMesa(mesa);

        // Guardar la reserva actualizada en la base de datos
        reservaService.guardarReserva(reservaExistente);

        return "redirect:/reservas/listaReserva"; // Redirige de vuelta a la lista de reservas
    }

   
}



