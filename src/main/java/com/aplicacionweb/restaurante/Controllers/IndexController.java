package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Service.CalificacionService;
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

import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.Pedido;
import com.aplicacionweb.restaurante.Models.TipoEntregaPedido;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.Reservas.MetodoDePago;
import com.aplicacionweb.restaurante.Service.MenuService;
import com.aplicacionweb.restaurante.Service.PedidoService;
import com.aplicacionweb.restaurante.Service.UserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UserService userService;

    @Autowired
    private CalificacionService calificacionService;

    // Método que muestra la página de inicio
    @GetMapping("/restaurante")
    public String mostrarInicio(Model model) {
        // Obtener el nombre del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre del usuario

        // Si el usuario está autenticado, agregar el nombre al modelo
        if (username != null) {
            model.addAttribute("username", username);
        }

        // Obtener todos los menús
        List<Menu> menus = menuService.obtenerTodosLosMenus();
        model.addAttribute("menus", menus); // Pasar los menús al modelo

        return "inicio_session"; // Retornar la vista
    }



    @GetMapping("/modoInvitado")
    public String mostrarInicioTodos(Model model) {

        List<Menu> menus = menuService.obtenerTodosLosMenus();
        model.addAttribute("menus", menus);

        Map<Long, Double> calificacionesPromedio = new HashMap<>();

        for (Menu menu : menus) {
            calificacionesPromedio.put(
                    menu.getId(),
                    calificacionService.obtenerPromedio(menu.getId())
            );
        }

        model.addAttribute("calificacionesPromedio", calificacionesPromedio);

        return "index";
    }




    @PostMapping("/pedidos")
    public String realizarPedido(@RequestParam Long menuId, @RequestParam Integer cantidad,
                                 @RequestParam TipoEntregaPedido tipoEntrega,
                                 @RequestParam MetodoDePago metodoDePago,
                                 Model model, RedirectAttributes redirectAttributes) {

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            model.addAttribute("error", "Debes estar logueado para hacer un pedido.");
            return "redirect:/login";
        }

        // Buscar el usuario en la base de datos
        User usuario = userService.buscarByUsername(username);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "inicio_session";
        }

        // Obtener el menú por ID
        Optional<Menu> optionalMenu = menuService.getMenuById(menuId);

        if (optionalMenu.isPresent()) {
            Menu menu = optionalMenu.get();

            // Calcular total correctamente con BigDecimal
            BigDecimal total = menu.getPrecio().multiply(BigDecimal.valueOf(cantidad));

            // Validar saldo
            if (usuario.getSaldo() == null || usuario.getSaldo().compareTo(total) < 0) {
                redirectAttributes.addFlashAttribute("error", "Saldo insuficiente para realizar el pedido.");
                return "redirect:/detalle-plato/" + menu.getId();
            }

            // Descontar saldo
            userService.descontarSaldo(usuario, total);

            // Crear pedido
            Pedido pedido = new Pedido();
            pedido.setFecha(new Date());
            pedido.setCantidad(cantidad);
            pedido.setTipoEntregaPedido(tipoEntrega);
            pedido.setTotal(total);
            pedido.setMenu(menu);
            pedido.setUsuario(usuario);
            pedido.setMetodoDePago(metodoDePago);

            // Guardar el pedido
            pedidoService.guardarPedido(pedido);

            redirectAttributes.addFlashAttribute("mensaje",
                    "Pedido del plato: " + menu.getNombreDePlato() + " realizado con éxito!");

            return "redirect:/detalle-plato/" + menu.getId();

        } else {
            model.addAttribute("error", "Menú no encontrado.");
            return "inicio_session";
        }
    }

    @GetMapping("/detalle-plato/{menuId}")
    public String mostrarDetallePedido(@PathVariable Long menuId, Model model) {
        // Obtener el plato (menú) usando el ID
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);

        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();

            // Agregar el plato al modelo
            model.addAttribute("menu", menu);

            // Obtener todos los menús de la misma categoría
            List<Menu> relacionados = menuService.buscarPorCategoria(menu.getCategoria());

            // Filtrar para excluir el plato actual
            relacionados.removeIf(m -> m.getId().equals(menuId));

            // Agregar los relacionados al modelo
            model.addAttribute("relacionados", relacionados);

            return "detalle_plato"; // Retorna la vista de detalle
        } else {
            model.addAttribute("error", "Plato no encontrado.");
            return "inicio_session"; // Si no se encuentra, redirige a inicio
        }
    }

}
