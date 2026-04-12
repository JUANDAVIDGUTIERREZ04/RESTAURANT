package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Calificacion;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.CalificacionRepository;
import com.aplicacionweb.restaurante.Repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final MenuRepository menuRepository;

    public CalificacionService(CalificacionRepository calificacionRepository,
                               MenuRepository menuRepository) {
        this.calificacionRepository = calificacionRepository;
        this.menuRepository = menuRepository;
    }

    public void calificar(Long menuId, User usuario, int estrellas) {

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menú no encontrado"));

        calificacionRepository.findByMenuAndUsuario(menu, usuario)
                .ifPresentOrElse(
                        calificacion -> {
                            // Si ya calificó → actualiza
                            calificacion.setEstrellas(estrellas);
                            calificacionRepository.save(calificacion);
                        },
                        () -> {
                            // Nueva calificación
                            Calificacion nueva = new Calificacion();
                            nueva.setMenu(menu);
                            nueva.setUsuario(usuario);
                            nueva.setEstrellas(estrellas);
                            calificacionRepository.save(nueva);
                        }
                );
    }

    public Double obtenerPromedio(Long menuId) {
        Double promedio = calificacionRepository.promedioPorMenu(menuId);
        return promedio != null ? promedio : 0.0;
    }
}
