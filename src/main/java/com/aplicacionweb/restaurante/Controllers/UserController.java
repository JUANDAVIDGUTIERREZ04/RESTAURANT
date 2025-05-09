package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.DTO.UserDto;
import com.aplicacionweb.restaurante.Service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        return "registro_usuario"; // Nombre del archivo HTML del formulario
    }

    @PostMapping("/registrar")
    public String createUser(@RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam int edad,
            @RequestParam String sexo,
            Model model) {

        try {
            // Verificar si el username ya existe
            if (userService.existsByNombre(username)) {
                model.addAttribute("error", "El nombre de usuario ya está en uso. Por favor, elige otro.");
                return "registro_usuario"; // Volver al formulario de registro con el mensaje de error
            }

            // Si no existe, proceder con el registro del nuevo usuario
            User user = new User();
            user.setNombre(nombre);
            user.setCorreo(correo);
            user.setTelefono(telefono);
            user.setUsername(username);
            user.setPassword("{noop}" + password);
            user.setSexo(sexo);
            user.setEdad(edad);// Añadir {noop} a la contraseña
            user.setRole(role);

            userService.saveUser(user); // Guardar el usuario

            return "index"; // Redirigir a la página de inicio después de registrar
        } catch (Exception e) {
            return "Error al registrar el usuario" + e.getMessage();

        }
    }

    // obtener lista de usuarios
    @GetMapping("/usuarios")
    public String mostrarListaUsuarios(Model model) {
        List<UserDto> usuarios = userService.obtenerTodosLosUsuarios(); // Obtener la lista de usuarios
        model.addAttribute("usuarios", usuarios); // Agregar la lista al modelo
        return "usuario_lista"; // Nombre del archivo HTML sin extensión
    }

    // eliminar usuario por id
    @PostMapping("/usuarios/eliminar")
    public String eliminarUsuario(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        if (userService.exitById(userId)) {
            userService.eliminarUsuario(userId);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/modificar/{id}")
    public String obtenerUsuarioId(@PathVariable Long id, Model model) {
        User user = userService.getById(id).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "usuario_lista"; // O redirigir a la lista de usuarios
        }
        model.addAttribute("user", user); // Agregar el usuario al modelo
        return "modificar_usuario"; // Nombre del archivo HTML del formulario
    }

    @PostMapping("/usuarios/modificar/{id}")
    public String modificarUsuario(@PathVariable Long id, @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {
        // Crear el objeto con los valores recibidos
        User updatedUser = new User();
        updatedUser.setNombre(nombre);
        updatedUser.setCorreo(correo);
        updatedUser.setTelefono(telefono);
        updatedUser.setUsername(username);
        updatedUser.setPassword(password); // Aquí estamos manteniendo la contraseña tal como la recibimos
        updatedUser.setRole(role);

        // Actualizar el usuario en el servicio
        userService.updateUser(id, updatedUser);

        // Agregar el mensaje de éxito
        redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente.");

        // Redirigir a la lista de usuarios
        return "redirect:/usuarios";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        // Obtener el usuario autenticado desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre de usuario del principal

        // Buscar el usuario en la base de datos a partir del username
        User user = userService.buscarByUsername(username);

        // Asegurarnos de que el usuario no sea null antes de pasarlo al modelo
        if (user != null) {
            model.addAttribute("user", user); // Pasamos el objeto user al modelo
        } else {
            model.addAttribute("error", "Usuario no encontrado");
        }

        return "perfil"; // Nombre de la vista (perfil.html)
    }

    // Método para buscar usuarios por ID
    @GetMapping("/usuarios/buscar/resultados")
    public String buscarUsuarios(@RequestParam Long userId, Model model) {
        UserDto usuarioDto = userService.buscarUsuarioPorId(userId);
        if (usuarioDto == null) {
            model.addAttribute("mensaje", "Usuario no encontrado.");
        } else {
            model.addAttribute("usuario", usuarioDto); // Agregar el usuario encontrado al modelo
        }
        List<UserDto> usuarios = userService.obtenerTodosLosUsuarios(); // Obtener todos los usuarios
        model.addAttribute("usuarios", usuarios); // Agregar la lista de usuarios al modelo
        return "usuario_lista"; // Devolver la misma vista con el resultado de la búsqueda
    }

}
