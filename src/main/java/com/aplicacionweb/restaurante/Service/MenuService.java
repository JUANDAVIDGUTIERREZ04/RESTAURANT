package com.aplicacionweb.restaurante.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Repository.MenuRepository;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    
    // Registrar menú
    public Menu RegistrarMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    // Obtener menú por ID
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    // Eliminar menú
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public List<Menu> obtenerTodosLosMenus() {
        return menuRepository.findAll();
    }

    public Page<Menu> obtenerMenusPaginados(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    return menuRepository.findAll(pageable);
}


     // Método para eliminar un menú por su ID
     public void eliminarMenu(Long id) {
        // Verifica si el menú existe antes de eliminarlo
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);  // Elimina el menú de la base de datos
        } else {
            throw new RuntimeException("El menú con ID " + id + " no existe.");
        }
    }
}
