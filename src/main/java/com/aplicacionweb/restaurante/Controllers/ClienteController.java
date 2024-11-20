package com.aplicacionweb.restaurante.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Service.PedidoService;
import com.aplicacionweb.restaurante.Service.UserService;

@Controller
public class ClienteController {

    @Autowired
    private UserService userService;

    @Autowired
    private PedidoService pedidoService;

    // Mostrar el perfil del usuario
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        // Obtener el nombre de usuario del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre de usuario autenticado

        // Buscar el usuario en la base de datos
        User usuario = userService.buscarByUsername(username);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login"; // Redirigir al login si no se encuentra el usuario
        }

        // Añadir los atributos al modelo para ser utilizados en la vista
        model.addAttribute("usuario", usuario);
        model.addAttribute("pedidos", pedidoService.obtenerPedidosPorUsuario(usuario)); // Listar los pedidos del usuario
        return "perfil"; // Retornar la vista del perfil del usuario
    }

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
