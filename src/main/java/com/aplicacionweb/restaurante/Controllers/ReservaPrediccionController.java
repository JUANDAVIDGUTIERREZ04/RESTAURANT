package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Reservas.Reserva;
import com.aplicacionweb.restaurante.Models.Reservas.ReservaDTO;
import com.aplicacionweb.restaurante.Service.PrediccionReservaService;
import com.aplicacionweb.restaurante.Service.ReservaService;


import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;



@Controller
@RequestMapping("/prediccionReservas")
public class ReservaPrediccionController {

    @Autowired
    private PrediccionReservaService prediccionReservaService;

    @Autowired
    private ReservaService reservaService;

    // Muestra el formulario
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("reservaDTO", new ReservaDTO());
        return "form_prediccion_reserva";
    }

    // Procesa la predicción
    @PostMapping("/predecir")
    public String predecirEstadoReserva(@ModelAttribute ReservaDTO reservaDTO, Model model) {
        try {
            String resultado = prediccionReservaService.predecirEstadoReserva(reservaDTO);

            // Ejemplo resultado: "Estado predicho: cancelada | Probabilidad de cancelación: 65.00%"
            String[] partes = resultado.split("\\|");
            String estado = partes[0].split(":")[1].trim();
            String probabilidadTexto = partes[1].split(":")[1].trim().replace("%", "").replace(",", "."); // manejo robusto

            double probabilidad = Double.parseDouble(probabilidadTexto);

            model.addAttribute("resultado", estado);
            model.addAttribute("probabilidad", probabilidad);
            model.addAttribute("reservaDTO", reservaDTO);

        } catch (Exception e) {
            model.addAttribute("error", "Error al realizar la predicción: " + e.getMessage());
        }

        return "resultado_prediccion";
    }

    @GetMapping("/ver/{id}")
public String predecirDesdeReserva(@PathVariable Long id, Model model) {
    ReservaDTO dto = new ReservaDTO();

    try {
        Optional<Reserva> optionalReserva = reservaService.findById(id);

        if (optionalReserva.isEmpty()) {
            model.addAttribute("error", "No se encontró la reserva con ID: " + id);
            model.addAttribute("reservaDTO", dto);
            return "resultado_prediccion";
        }

        Reserva reserva = optionalReserva.get();

        // Construcción del DTO
        dto.setNumeroPersonas(reserva.getNumeroPersonas());
        dto.setMetodoDePago(reserva.getMetodoDePago().toString());
        dto.setClienteRecurrente(reserva.getClienteRecurrente());
        dto.setAnticipacion(reserva.getAnticipacion().intValue());

        // Día en inglés (asegurarse de usar el Locale.ENGLISH)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH); // Forzar inglés
        String diaEnIngles = reserva.getFecha().format(formatter); // Ej: "Monday"

        // Imprimir el valor de diaEnIngles para depurar
        System.out.println("Día en inglés obtenido: " + diaEnIngles);
        
        // Traducir el día al español
        String diaEnEspanol = traducirDiaSemana(diaEnIngles); // Ej: "LUNES"
        
        // Verificar si la traducción fue exitosa
        if (diaEnEspanol == null || diaEnEspanol.isEmpty()) {
            model.addAttribute("error", "No se pudo traducir el día de la semana. Día en inglés: " + diaEnIngles);
            model.addAttribute("reservaDTO", dto);
            return "resultado_prediccion";
        }

        dto.setDiaSemana(diaEnEspanol);

        // Validación de campos
        if (dto.getNumeroPersonas() == 0 || dto.getMetodoDePago() == null || dto.getDiaSemana() == null) {
            model.addAttribute("error", "Datos incompletos en la reserva para la predicción.");
            model.addAttribute("reservaDTO", dto);
            return "resultado_prediccion";
        }

        // Predicción
        String resultadoCompleto = prediccionReservaService.predecirEstadoReserva(dto);

        if (resultadoCompleto.contains("Estado predicho:") && resultadoCompleto.contains("Probabilidad de cancelación:")) {
            String[] partes = resultadoCompleto.split("\\|");

            if (partes.length < 2) {
                throw new IllegalArgumentException("Formato de resultado inválido. No se encontraron las partes esperadas.");
            }

            String probabilidadTexto = partes[1].split(":")[1].trim().replace("%", "").replace(",", ".");
            double probabilidad = Double.parseDouble(probabilidadTexto);

            model.addAttribute("reservaDTO", dto);
            model.addAttribute("resultado", resultadoCompleto);
            model.addAttribute("probabilidad", probabilidad);
        } else {
            model.addAttribute("error", "El formato de la predicción es incorrecto.");
            model.addAttribute("reservaDTO", dto);
        }

    } catch (Exception e) {
        model.addAttribute("error", "Error al realizar la predicción: " + e.getMessage());
        model.addAttribute("reservaDTO", dto);
    }

    return "resultado_prediccion";
}

// Método para traducir los días de la semana de inglés a español
private String traducirDiaSemana(String diaEnIngles) {
    switch (diaEnIngles.toUpperCase()) {
        case "MONDAY": return "LUNES";
        case "TUESDAY": return "MARTES";
        case "WEDNESDAY": return "MIERCOLES";
        case "THURSDAY": return "JUEVES";
        case "FRIDAY": return "VIERNES";
        case "SATURDAY": return "SABADO";
        case "SUNDAY": return "DOMINGO";
        default: return null; // Si no es un día válido, retornar null
    }
}

    

}





    

