package com.aplicacionweb.restaurante.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aplicacionweb.restaurante.Models.Calificacion;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.CalificacionRepository;
import com.aplicacionweb.restaurante.Repository.MenuRepository;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private MenuRepository menuRepository;

    // Método para agregar una calificación
    public void agregarCalificacion(Long menuId, User usuario, int estrellas) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menú no encontrado"));
        Calificacion calificacion = new Calificacion();
        calificacion.setMenu(menu);
        calificacion.setUsuario(usuario);
        calificacion.setEstrellas(estrellas);

        calificacionRepository.save(calificacion);

        // Recalcular el promedio de calificaciones
        recalcularPromedio(menu);
    }

    // Método para recalcular el promedio de calificación
    private void recalcularPromedio(Menu menu) {
        double total = 0;
        int cantidad = 0;

        // Obtener todas las calificaciones para el menú
        for (Calificacion calificacion : calificacionRepository.findByMenu(menu)) {
            total += calificacion.getEstrellas();
            cantidad++;
        }

        // Calcular el promedio
        double promedio = (cantidad > 0) ? total / cantidad : 0;

        // Actualizar el campo calificacionPromedio
        menu.setCalificacionPromedio(promedio);
        menuRepository.save(menu);
    }
}
