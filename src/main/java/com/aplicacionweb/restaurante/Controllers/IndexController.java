package com.aplicacionweb.restaurante.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.Pedido;
import com.aplicacionweb.restaurante.Models.TipoEntregaPedido;
import com.aplicacionweb.restaurante.Models.User; 
import com.aplicacionweb.restaurante.Service.MenuService;
import com.aplicacionweb.restaurante.Service.PedidoService;
import com.aplicacionweb.restaurante.Service.UserService;



import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UserService userService;

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

        // Obtener todos los menús
        List<Menu> menus = menuService.obtenerTodosLosMenus(); 
        model.addAttribute("menus", menus); 

        return "index"; // Retornar la vista
    }

    // Método que maneja la realización de un pedido
    @PostMapping("/pedidos")
    public String realizarPedido(@RequestParam Long menuId, @RequestParam Integer cantidad, @RequestParam TipoEntregaPedido tipoEntrega, Model model , RedirectAttributes redirectAttributes) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre del usuario autenticado

        if (username == null) {
            model.addAttribute("error", "Debes estar logueado para hacer un pedido.");
            return "redirect:/login"; // Redirigir a login si no está autenticado
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
            Pedido pedido = new Pedido();
            pedido.setFecha(new Date());
            pedido.setCantidad(cantidad);
            pedido.setTipoEntregaPedido(tipoEntrega);
            pedido.setTotal(menu.getPrecio() * cantidad);
            pedido.setMenu(menu);
            pedido.setUsuario(usuario); // Asociar el pedido al usuario autenticado
            
            // Guardar el pedido
            pedidoService.guardarPedido(pedido);
            
            redirectAttributes.addFlashAttribute("mensaje", "Pedido del plato: "+ menu.getNombreDePlato() + " realizado con éxito!");
            return "redirect:/restaurante"; // Redirigir a la página de inicio con el mensaje de éxito
        } else {
            model.addAttribute("error", "Menú no encontrado.");
            return "inicio_session"; // Si no se encuentra el menú, mostrar un error
        }
    }
}
