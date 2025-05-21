package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Mesas.*;
import com.aplicacionweb.restaurante.Service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // Mostrar las mesas disponibles
    @GetMapping("/disponibles")
    public String mostrarMesasDisponibles(Model model) {
        List<Mesa> mesasDisponibles = mesaService.obtenerTodasLasMesasConDisponibilidadActual();
        model.addAttribute("mesas", mesasDisponibles);
        return "mesas/lista_mesas";
    }

    // Mostrar detalles de una mesa
    @GetMapping("/{id}")
    public String verMesa(@PathVariable Long id, Model model) {
        Optional<Mesa> mesa = mesaService.obtenerMesaPorId(id);
        if (mesa.isPresent()) {
            model.addAttribute("mesa", mesa.get());
            return "detalle_mesa"; // Vista que muestra detalles de una mesa
        }
        model.addAttribute("error", "Mesa no encontrada");
        return "error";
    }

    // Cambiar la disponibilidad de una mesa
    @PostMapping("/cambiarDisponibilidad/{id}")
    public String cambiarDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        Optional<Mesa> mesaOptional = mesaService.obtenerMesaPorId(id);

        if (mesaOptional.isPresent()) {
            Mesa mesa = mesaOptional.get();
            // Cambiar la disponibilidad de la mesa
            mesa.setDisponible(disponible);
            // Guardar la mesa actualizada en la base de datos
            mesaService.guardarMesa(mesa);
        } else {
            // Si no se encuentra la mesa, lanzar excepción o manejar el error de forma
            // adecuada
            throw new NoSuchElementException("Mesa no encontrada");
        }

        // Redirigir de vuelta a la lista de mesas disponibles o a otra página después
        // de actualizar
        return "redirect:/mesas/disponibles";
    }

    @GetMapping("/nuevo")
    public String formularioNuevaMesa(Model model) {
        model.addAttribute("mesa", new Mesa()); // Inicializa un nuevo objeto Mesa
        model.addAttribute("secciones", SeccionesDeMesas.values()); // Pasamos las secciones
        return "/mesas/form_mesa"; // Vista para agregar una nueva mesa
    }

    // Guardar una nueva mesa
    @PostMapping("/guardar")
    public String guardarMesa(@ModelAttribute Mesa mesa, Model model) {
        mesa.setPrecio(); // Establecemos el precio según la sección seleccionada
        mesaService.guardarMesa(mesa); // Llama al servicio para guardar la mesa

        // Agregar un mensaje de éxito al modelo
        model.addAttribute("mensaje", "Mesa registrada con éxito.");
        return "mesas/form_mesa"; // Redirige de nuevo al formulario o muestra el mensaje de éxito
    }

    // Eliminar una mesa
    @PostMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
        return "redirect:/mesas/disponibles"; // Redirigir a la lista de mesas disponibles
    }
}
