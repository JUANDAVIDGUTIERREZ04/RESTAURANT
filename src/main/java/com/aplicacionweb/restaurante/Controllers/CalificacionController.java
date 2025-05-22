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


@Controller
public class CalificacionController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CalificacionService calificacionService;

     @PostMapping("/calificar")
    public String calificarPlato(@RequestParam Long menuId, @RequestParam int estrellas) {
        // Obtener el usuario autenticado manualmente usando SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre de usuario del usuario autenticado
        
        if (username == null) {
            return "Debes estar registrado para calificar";
        }

        // Obtener el usuario del repositorio usando el nombre de usuario
        User usuario = userService.buscarByUsername(username);
        
        if (usuario == null) {
            return "Usuario no encontrado";
        }

        if (estrellas < 1 || estrellas > 5) {
            return "La calificación debe ser entre 1 y 5 estrellas";
        }

        // Pasar el usuario al servicio para agregar la calificación
        calificacionService.agregarCalificacion(menuId, usuario, estrellas);

        return "redirect:/restaurante";  // Redirige después de calificar
    }

    @GetMapping("/detalle/{menuId}")
public String detallePlato(@PathVariable Long menuId, Model model) {
    // Obtener el plato con su id
    Menu menu = menuService.buscarPorId(menuId);
    // Obtener la calificación promedio del plato
    double calificacionPromedio = menu.getCalificacionPromedio();

    // Agregar el menú y la calificación promedio al modelo
    model.addAttribute("menu", menu);
    model.addAttribute("calificacionPromedio", calificacionPromedio);

    return "detalle_plato"; // Retorna la vista correspondiente
}
}
