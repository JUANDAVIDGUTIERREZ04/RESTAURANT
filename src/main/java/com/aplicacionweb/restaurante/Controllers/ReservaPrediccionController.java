package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Prediccion;
import com.aplicacionweb.restaurante.Models.Reservas.ReservaDTO;
import com.aplicacionweb.restaurante.Service.PrediccionReservaService;
import com.aplicacionweb.restaurante.Repository.PrediccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/prediccionReservas")
public class ReservaPrediccionController {

    @Autowired
    private PrediccionReservaService prediccionReservaService;

    @Autowired
    private PrediccionRepository prediccionRepository;

    // Muestra el formulario de predicción
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("reservaDTO", new ReservaDTO()); // Objeto vacío para el formulario
        return "form_prediccion_reserva"; // Nombre de la vista Thymeleaf
    }

    // Procesa la predicción de si la reserva será cancelada o no
    @PostMapping("/predecir")
    public String predecirEstadoReserva(@ModelAttribute ReservaDTO reservaDTO, Model model) {
        try {
            // Llamada al servicio para obtener la predicción
            String resultado = prediccionReservaService.predecirEstadoReserva(reservaDTO);

            // Guardar la predicción en la base de datos
            Prediccion prediccion = new Prediccion();
            prediccion.setNumeroPersonas(reservaDTO.getNumeroPersonas());
            prediccion.setOrigenReserva(reservaDTO.getOrigenReserva());
            prediccion.setMetodoPago(reservaDTO.getMetodoDePago());
            prediccion.setClienteRecurrente(reservaDTO.getClienteRecurrente());
            prediccion.setAnticipacion(reservaDTO.getAnticipacion());
            prediccion.setDiaSemana(reservaDTO.getDiaSemana());
            prediccion.setEstadoReserva(resultado); // Cambio aquí a estadoReserva
            prediccion.setFecha(LocalDateTime.now()); // Fecha actual

            // Guardar la predicción en el repositorio
            prediccionRepository.save(prediccion);

            // Pasar el resultado y los datos al modelo
            String resultadoTraducido = resultado.equalsIgnoreCase("pagada") ? "cancelada" : "no cancelada";
model.addAttribute("resultado", resultadoTraducido);

            model.addAttribute("reservaDTO", reservaDTO); // Se reenvía para mostrar lo ingresado

        } catch (Exception e) {
            model.addAttribute("error", "Error al realizar la predicción: " + e.getMessage());
        }
        return "resultado_prediccion"; // Vista que muestra el resultado
    }

    // Método para mostrar la tabla de predicciones
    @GetMapping("/tablaPredicciones")
    public String mostrarTablaPredicciones(Model model) {
        List<Prediccion> predicciones = prediccionRepository.findAll(); // Obtener todas las predicciones
        model.addAttribute("predicciones", predicciones); // Añadir las predicciones al modelo
        return "lista_predicciones"; // Nombre de la vista Thymeleaf
    }
}
