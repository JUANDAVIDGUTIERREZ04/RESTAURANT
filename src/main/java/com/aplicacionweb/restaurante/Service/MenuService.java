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
    public Menu registrarMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    // Método para actualizar un menú
    public Menu actualizarMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    // Método para buscar un menú por su ID
    public Menu buscarPorId(Long id) {
        return menuRepository.findById(id).orElse(null); // Si no se encuentra el menú, se retorna null
    }

    // Obtener menú por ID
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    // Eliminar menú
    public void eliminarMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public List<Menu> obtenerTodosLosMenus() {
        return menuRepository.findAll();
    }

    public Page<Menu> obtenerMenusPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return menuRepository.findAll(pageable);
    }
}
