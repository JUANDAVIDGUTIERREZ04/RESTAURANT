package com.aplicacionweb.restaurante.Controllers;


import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Service.CalificacionService;
import com.aplicacionweb.restaurante.Service.MenuService;
import com.aplicacionweb.restaurante.Service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class CalificacionController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/calificar")
    public String calificarPlato(
            @RequestParam Long menuId,
            @RequestParam int estrellas
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User usuario = userService.buscarByUsername(username);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (estrellas < 1 || estrellas > 5) {
            return "redirect:/detalle/" + menuId;   // ✅ CAMBIO AQUÍ
        }

        calificacionService.calificar(menuId, usuario, estrellas);

        return "redirect:/detalle/" + menuId;       // ✅ Y AQUÍ
    }





}
