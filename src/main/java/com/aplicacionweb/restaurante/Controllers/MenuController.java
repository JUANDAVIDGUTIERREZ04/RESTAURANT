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

    // =========================
    // LISTAR
    // =========================
    @GetMapping("/listar")
    public String listarMenus(@RequestParam(defaultValue = "0") int page, Model model) {

        int pageSize = 5;
        Page<Menu> menuPage = menuService.obtenerMenusPaginados(page, pageSize);

        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("totalPages", menuPage.getTotalPages());
        model.addAttribute("currentPage", page);

        model.addAttribute("menu", new Menu());

        return "menu_lista";
    }

    // =========================
    // CREAR MENU
    // =========================
    @PostMapping
    public String registrarMenu(@ModelAttribute Menu menu,
                                @RequestParam("imagen") MultipartFile archivo) {
        try {

            File directorio = new File(directorioImagenes);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            if (archivo != null && !archivo.isEmpty()) {

                String nombreArchivo =
                        System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

                Path rutaDestino = Path.of(directorioImagenes, nombreArchivo);
                archivo.transferTo(rutaDestino.toFile());

                menu.setImagenUrl("/imagenes/" + nombreArchivo);
            }

            menuService.registrarMenu(menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/adminMenu/listar";
    }

    // =========================
    // ELIMINAR
    // =========================
    @GetMapping("/eliminar/{id}")
    public String eliminarMenu(@PathVariable Long id) {
        menuService.eliminarMenu(id);
        return "redirect:/adminMenu/listar";
    }

    // =========================
    // FORM EDITAR
    // =========================
    @GetMapping("/editar/{id}")
    public String editarMenu(@PathVariable Long id, Model model) {

        Menu menu = menuService.buscarPorId(id);

        if (menu == null) {
            return "redirect:/adminMenu/listar";
        }

        model.addAttribute("menu", menu);

        return "editarMenu";
    }

    // =========================
    // ACTUALIZAR MENU
    // =========================
    @PostMapping("/editar/{id}")
    public String actualizarMenu(@PathVariable Long id,
                                 @ModelAttribute Menu menu,
                                 @RequestParam(value = "imagen", required = false) MultipartFile archivo) {
        try {

            File directorio = new File(directorioImagenes);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            if (archivo != null && !archivo.isEmpty()) {

                String nombreArchivo =
                        System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

                Path rutaDestino = Path.of(directorioImagenes, nombreArchivo);
                archivo.transferTo(rutaDestino.toFile());

                menu.setImagenUrl("/imagenes/" + nombreArchivo);
            }

            menu.setId(id);

            menuService.actualizarMenu(menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/adminMenu/listar";
    }
}