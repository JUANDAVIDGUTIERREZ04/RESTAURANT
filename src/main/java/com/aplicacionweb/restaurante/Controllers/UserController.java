package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.DTO.UserDto;
import com.aplicacionweb.restaurante.Service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            // Verificar si el nombre de usuario ya está en uso
            if (userService.existsByNombre(username)) {
                model.addAttribute("error", "El nombre de usuario ya está en uso. Por favor, elige otro.");
                return "registro_usuario";
            }

            // Crear un nuevo usuario
            User user = new User();
            user.setNombre(nombre);
            user.setCorreo(correo);
            user.setTelefono(telefono);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));

            user.setSexo(sexo);
            user.setEdad(edad);
            user.setRole(role); // Rol de usuario

            userService.saveUser(user); // Guardar el usuario en la base de datos

            return "index"; // Redirigir a la página de inicio después del registro
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "registro_usuario"; // Retornar a la página de registro con error
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

    @PostMapping("/usuarios/actualizar/{id}")
    public String modificarUsuario(@PathVariable Long id, @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam String sexo,
            RedirectAttributes redirectAttributes) {
        // Crear el objeto con los valores recibidos
        User updatedUser = new User();
        updatedUser.setNombre(nombre);
        updatedUser.setCorreo(correo);
        updatedUser.setTelefono(telefono);
        updatedUser.setSexo(sexo);
        updatedUser.setUsername(username);
        updatedUser.setPassword(passwordEncoder.encode(password));
        // Aquí estamos manteniendo la contraseña tal como la recibimos
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
    @GetMapping("/usuarios/buscar")
public String buscarUsuariosConPaginacion(@RequestParam Long userId, Pageable pageable, Model model) {
    Page<UserDto> usuariosPage = userService.buscarUsuariosPorIdConPaginacion(userId, pageable);

    model.addAttribute("usuariosBuscados", usuariosPage.getContent());
    model.addAttribute("totalPages", usuariosPage.getTotalPages());
    model.addAttribute("currentPage", usuariosPage.getNumber());
    return "usuario_lista";
}




    // obterner todos los usuarios activos
    @GetMapping("/usuarios/activos")
public String mostrarUsuariosActivos(@RequestParam(defaultValue = "0") int page, Model model) {
    int pageSize = 6;
    PageRequest pageable = PageRequest.of(page, pageSize);
    Page<UserDto> usuariosActivosPage = userService.obtenerUsuariosActivosPaginados(pageable);

    model.addAttribute("usuariosActivos", usuariosActivosPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", usuariosActivosPage.getTotalPages());

    return "usuario_lista";
}


    // Método para marcar como inactivo (en lugar de eliminar)
    @PostMapping("/usuarios/inactivar")
    public String inactivarUsuario(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        if (userService.exitById(userId)) {
            userService.marcarUsuarioInactivo(userId); // Marcamos al usuario como inactivo
            redirectAttributes.addFlashAttribute("mensaje", "Usuario marcado como inactivo exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/usuarios/activos";
    }

    @PostMapping("/usuarios/actualizar-modal/{id}")
public String actualizarDesdeModal(
        @PathVariable Long id,
        @RequestParam String nombre,
        @RequestParam String correo,
        @RequestParam String telefono,
        @RequestParam int edad,
        @RequestParam String sexo,
        @RequestParam String username,
        @RequestParam(required = false) String password,
        @RequestParam String role,
        RedirectAttributes redirectAttributes) {

    User userExistente = userService.getById(id).orElse(null);
    if (userExistente == null) {
        redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        return "redirect:/usuarios";
    }

    // Actualizar campos
    userExistente.setNombre(nombre);
    userExistente.setCorreo(correo);
    userExistente.setTelefono(telefono);
    userExistente.setEdad(edad);
    userExistente.setSexo(sexo);
    userExistente.setUsername(username);
    userExistente.setRole(role);

    if (password != null && !password.isEmpty()) {
        userExistente.setPassword(passwordEncoder.encode(password));
    }

    userService.saveUser(userExistente);

    redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente.");
    return "redirect:/usuarios/activos";
}


}
