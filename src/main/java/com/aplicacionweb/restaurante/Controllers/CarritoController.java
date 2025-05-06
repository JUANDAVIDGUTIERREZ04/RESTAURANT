package com.aplicacionweb.restaurante.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.DetallePedido;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.Reservas.MetodoDePago;
import com.aplicacionweb.restaurante.Service.CarritoService;
import com.aplicacionweb.restaurante.Service.DetallePedidoService;
import com.aplicacionweb.restaurante.Service.MenuService;
import com.aplicacionweb.restaurante.Service.UserService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @Autowired
    private DetallePedidoService detallePedidoService;

    @PostMapping("/agregar")
    public String agregarAlCarrito(
            @RequestParam("menuId") Long menuId,
            @RequestParam("cantidad") Integer cantidad,
            
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }
    
        String username = authentication.getName();
        User usuario = userService.buscarByUsername(username);
    
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/login";
        }
    
        Menu menu = menuService.getMenuById(menuId)
                .orElseThrow(() -> new RuntimeException("Menú no encontrado"));
    
        // Validación de la cantidad
        if (cantidad == null || cantidad <= 0) {
            redirectAttributes.addFlashAttribute("error", "Cantidad no válida.");
            return "redirect:/carrito/lista";
        }
    
        // Lógica para agregar el ítem al carrito
        try {
            carritoService.agregarItemAlCarrito(usuario, menu, cantidad);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el producto al carrito.");
            return "redirect:/carrito/lista";
        }
    
        redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return "redirect:/carrito/lista";
    }
    


    // Ver el carrito de un usuario
    @GetMapping("/lista")
    public String verCarrito(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        String username = authentication.getName();
        User usuario = userService.buscarByUsername(username);

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login";
        }

        List<CarritoItem> itemsCarrito = carritoService.obtenerListaCarrito(usuario);

        double subtotal = itemsCarrito.stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();

        int cantidadTotalIcono = itemsCarrito.stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();

        model.addAttribute("itemsCarrito", itemsCarrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("cantidadTotalIcono", cantidadTotalIcono);

        return "carrito"; // Devolver la vista carrito.html
    }

    // Finalizar compra
    @PostMapping("/finalizar-compra")
public String finalizarCompra(@RequestParam("tipoEntrega") String tipoEntrega, 
                              @RequestParam("direccion") String direccion, 
                              @RequestParam("metodoDePago") String metodoDePago, 
                              Authentication authentication, 
                              Model model) {

    if (authentication == null || !authentication.isAuthenticated()) {
        return "redirect:/login"; // Redirigir al login si no está autenticado
    }

    String username = authentication.getName();
    User usuario = userService.buscarByUsername(username);

    if (usuario == null) {
        model.addAttribute("error", "Usuario no encontrado.");
        return "redirect:/login"; // Redirigir al login si no se encuentra el usuario
    }

    List<CarritoItem> itemsCarrito = carritoService.obtenerListaCarrito(usuario);

    if (itemsCarrito.isEmpty()) {
        model.addAttribute("error", "El carrito está vacío.");
        return "redirect:/carrito/lista"; // Redirigir si el carrito está vacío
    }

    // Crear los detalles del pedido
    List<DetallePedido> detallesPedido = new ArrayList<>();

    // Crear el detalle del pedido para cada ítem en el carrito
    for (CarritoItem item : itemsCarrito) {
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setCarritoItem(item);
        detallePedido.setTipoEntrega(tipoEntrega);
        detallePedido.setDireccion(direccion); // Asignar la dirección
        detallePedido.setMetodoDePago(MetodoDePago.valueOf(metodoDePago)); // Asignar el metodo de pago (enum)

        // Agregar el detalle al listado
        detallesPedido.add(detallePedido);
    }

    // Guardar los detalles del pedido en la base de datos
    detallePedidoService.guardarDetallesPedido(detallesPedido);

    // Pasar los detalles del pedido y tipo de entrega a la vista
    model.addAttribute("detallesPedido", detallesPedido);
    model.addAttribute("tipoEntrega", tipoEntrega);

    return "redirect:/carrito/listaDetallePedido";  // Vista para mostrar los detalles de la compra
}


    // Ver los detalles del pedido de un usuario
    @GetMapping("/listaDetallePedido")
    public String verDetallesPedidos(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        String username = authentication.getName();
        Long usuarioId = userService.buscarByUsername(username).getId();

        // Obtener los detalles de los pedidos del usuario
        List<DetallePedido> detallesPedidos = detallePedidoService.obtenerDetallesPorUsuario(usuarioId);

        model.addAttribute("detallesPedidos", detallesPedidos); // Pasamos los detalles a la vista

        return "detalles-pedidos";  // Vista donde se muestran los detalles de los pedidos
    }

    // Eliminar producto del carrito (solo elimina el ítem del carrito, no los detalles de pedido)
    @PostMapping("/eliminar/{itemId}")
    public String eliminarProductoDelCarrito(@PathVariable Long itemId, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        String username = authentication.getName();
        User usuario = userService.buscarByUsername(username);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/login";
        }

        // Llamar al servicio para eliminar el producto del carrito (no afecta los detalles del pedido)
        try {
            carritoService.eliminarProductoDelCarrito(itemId); // Eliminar el ítem por su ID
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito/lista"; // Redirigir a la lista del carrito
    }
}
