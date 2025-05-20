package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Service.MenuService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping("/adminMenu")
public class MenuController {

    @Value("${imagenes.directorio}")
    private String directorioImagenes;

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Método para mostrar el formulario de registro
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu_lista";  // Nombre de la vista Thymeleaf
    }

    // Método para listar menús con paginación
    @GetMapping("/listar")
    public String listarMenus(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5;
        Page<Menu> menuPage = menuService.obtenerMenusPaginados(page, pageSize);

        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("totalPages", menuPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "menu_lista";
    }

    // Método para procesar el formulario de registro y guardar la imagen
    @PostMapping
    public String registrarMenu(@ModelAttribute Menu menu, @RequestParam("imagen") MultipartFile archivo, Model model) {
        try {
            // Verifica si el directorio existe, si no, lo crea
            File directorio = new File(directorioImagenes);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Obtén el nombre del archivo y genera la ruta
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || nombreArchivo.isEmpty()) {
                model.addAttribute("error", "Debe seleccionar una imagen");
                return "menu_lista";
            }

            // Guarda el archivo en el directorio
            Path rutaDestino = Path.of(directorioImagenes, nombreArchivo);
            archivo.transferTo(rutaDestino.toFile());

            // Establecer la URL o ruta relativa de la imagen
            String imagenUrl = "/imagenes/" + nombreArchivo;

            // Guardar el menu con la URL de la imagen
            menu.setImagenUrl(imagenUrl);
            menuService.registrarMenu(menu);

            model.addAttribute("message", "Menú registrado con éxito");
        } catch (IOException e) {
            model.addAttribute("error", "Error al subir la imagen");
        }

        return "redirect:/adminMenu/listar";  // Volver a la lista de menús después de registrar
    }

    // Método para eliminar un menú por su ID
    @GetMapping("/eliminar/{id}")
    public String eliminarMenu(@PathVariable Long id, Model model) {
        try {
            menuService.eliminarMenu(id);  // Llamamos al servicio para eliminar el menú
            model.addAttribute("message", "Menú eliminado con éxito.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el menú.");
        }
        return "redirect:/adminMenu/listar";  // Redirige a la lista de menús después de eliminar
    }

    // Método para mostrar el formulario de edición
    @GetMapping("/editar/{id}")
    public String editarMenu(@PathVariable("id") Long id, Model model) {
        // Buscar el menú por su id
        Menu menu = menuService.buscarPorId(id);
        if (menu != null) {
            model.addAttribute("menu", menu);
            return "editarMenu"; // Nombre de la vista Thymeleaf (editarMenu.html)
        }
        return "redirect:/adminMenu/listar"; // Redirige a la lista si no se encuentra el menú
    }

    // Método para procesar la edición del menú
    @PostMapping("/editar/{id}")
    public String actualizarMenu(@PathVariable("id") Long id, @ModelAttribute("menu") Menu menu, 
                                 @RequestParam(value = "imagen", required = false) MultipartFile archivo, Model model) {
        try {
            if (archivo != null && !archivo.isEmpty()) {
                // Verifica si el directorio existe, si no, lo crea
                File directorio = new File(directorioImagenes);
                if (!directorio.exists()) {
                    directorio.mkdirs();
                }

                // Obtén el nombre del archivo y genera la ruta
                String nombreArchivo = archivo.getOriginalFilename();
                Path rutaDestino = Path.of(directorioImagenes, nombreArchivo);
                archivo.transferTo(rutaDestino.toFile());

                // Establecer la URL de la nueva imagen
                String imagenUrl = "/imagenes/" + nombreArchivo;
                menu.setImagenUrl(imagenUrl);
            }

            // Actualizamos el menú
            menu.setId(id); // Asegúrate de mantener el id del menú
            menuService.actualizarMenu(menu);

            model.addAttribute("message", "Menú actualizado con éxito");
        } catch (IOException e) {
            model.addAttribute("error", "Error al subir la imagen");
        }

        return "redirect:/adminMenu/listar";  // Volver a la lista de menús después de actualizar
    }
}
