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
        model.addAttribute("reservaDTO", new ReservaDTO());  // Modelo vacío para la vista
        return "form_prediccion_reserva";  // Vista de entrada
    }

    // Procesa la predicción de cancelación
    @PostMapping("/predecir")
    public String predecirCancelacion(@ModelAttribute ReservaDTO reservaDTO, Model model) {
        try {
            // Realiza la predicción utilizando el servicio
            String resultado = prediccionReservaService.predecirCancelacion(reservaDTO);
            model.addAttribute("resultado", resultado);
            model.addAttribute("reservaDTO", reservaDTO);  // Resultado de la predicción
        } catch (Exception e) {
            // En caso de error, muestra el mensaje de error
            model.addAttribute("error", "Error al realizar la predicción: " + e.getMessage());
        }
        return "resultado_prediccion";  // Vista de resultados
    }
}
