package com.aplicacionweb.restaurante.Controllers;


import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Service.CalificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/calificar")
    public String calificarPlato(@RequestParam Long menuId, @RequestParam int estrellas, @AuthenticationPrincipal User usuario) {
        if (usuario == null) {
            return "Debes estar registrado para calificar";
        }

        if (estrellas < 1 || estrellas > 5) {
            return "La calificación debe ser entre 1 y 5 estrellas";
        }

        calificacionService.agregarCalificacion(menuId, usuario, estrellas);
        return "redirect:/restaurante";
    }
}
