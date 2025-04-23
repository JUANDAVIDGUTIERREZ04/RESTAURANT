package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Reservas.ReservaDTO;
import com.aplicacionweb.restaurante.Service.PrediccionReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prediccionReservas")
public class ReservaPrediccionController {

    @Autowired
    private PrediccionReservaService prediccionReservaService;

    // Muestra el formulario de predicción
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("reservaDTO", new ReservaDTO());  // Objeto vacío para el formulario
        return "form_prediccion_reserva";  // Nombre de la vista Thymeleaf
    }

    // Procesa la predicción de si la reserva será pagada o no
    @PostMapping("/predecir")
    public String predecirEstadoReserva(@ModelAttribute ReservaDTO reservaDTO, Model model) {
        try {
            // Llamada al servicio para obtener la predicción
            String resultado = prediccionReservaService.predecirEstadoReserva(reservaDTO);
            model.addAttribute("resultado", resultado);
            model.addAttribute("reservaDTO", reservaDTO);  // Se reenvía para mostrar lo ingresado
        } catch (Exception e) {
            model.addAttribute("error", "Error al realizar la predicción: " + e.getMessage());
        }
        return "resultado_prediccion";  // Vista que muestra el resultado
    }
}
