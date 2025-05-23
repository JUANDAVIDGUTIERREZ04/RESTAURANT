package com.aplicacionweb.restaurante.Controllers;

/* 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aplicacionweb.restaurante.Models.User;

import com.aplicacionweb.restaurante.Service.UserService;

@Controller
public class ClienteController {

    @Autowired
    private UserService userService;

    @Autowired
   

    

    // Actualizar el perfil del usuario
    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@RequestParam String nombre, @RequestParam String email, Model model) {
        // Obtener el nombre de usuario del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Buscar el usuario en la base de datos
        User usuario = userService.buscarByUsername(username);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login"; 
        }

        // Actualizar los datos del usuario
        usuario.setNombre(nombre);
        usuario.setCorreo(email);


        // Guardar los cambios en la base de datos
        userService.saveUser(usuario);

        // Añadir mensaje de éxito al modelo
        model.addAttribute("mensaje", "Perfil actualizado correctamente!");
        return "redirect:/perfil"; // Redirigir al perfil después de la actualización
    }
}
*/