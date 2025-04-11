package com.aplicacionweb.restaurante.Controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aplicacionweb.restaurante.Models.CarritoItem;
import com.aplicacionweb.restaurante.Models.DetallePedido;
import com.aplicacionweb.restaurante.Models.Menu;
import com.aplicacionweb.restaurante.Models.User;
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
        // Verificar que el usuario esté autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        // Obtenemos el username del usuario autenticado
        String username = authentication.getName();

        // Usamos UserService para obtener el usuario
        User usuario = userService.buscarByUsername(username);

        // Validamos que el usuario exista
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/login"; // Redirigir a login si no se encuentra el usuario
        }

        // Buscamos el menú por su ID
        Menu menu = menuService.getMenuById(menuId)
                .orElseThrow(() -> new RuntimeException("Menú no encontrado"));

        // Lógica para agregar el ítem al carrito
        carritoService.agregarItemAlCarrito(usuario, menu, cantidad);

        redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return "redirect:/restaurante";
    }

    // Obtener el carrito del usuario
    @GetMapping("/lista")
    public String verCarrito(Authentication authentication, Model model) {
        // Verificar que el usuario esté autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        // Obtener el username del usuario autenticado
        String username = authentication.getName();

        // Buscar el usuario por su username
        User usuario = userService.buscarByUsername(username);

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/login"; // Redirigir a login si no se encuentra el usuario
        }

        // Obtener la lista de productos en el carrito del usuario
        List<CarritoItem> itemsCarrito = carritoService.obtenerListaCarrito(usuario);

        // Calcular el subtotal del carrito
        double subtotal = itemsCarrito.stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();

        // Calcular la cantidad total de productos en el carrito
        int cantidadTotalIcono = itemsCarrito.stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();

        // Añadir la lista de productos y el subtotal al modelo
        model.addAttribute("itemsCarrito", itemsCarrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("cantidadTotalIcono", cantidadTotalIcono);

        return "carrito"; // Devolver la vista carrito.html
    }

    @PostMapping("/finalizar-compra")
public String finalizarCompra(String tipoEntrega, Authentication authentication, Model model) {
    // Verificar que el usuario esté autenticado
    if (authentication == null || !authentication.isAuthenticated()) {
        return "redirect:/login"; // Redirigir al login si no está autenticado
    }

    String username = authentication.getName(); // Obtén el nombre de usuario
    User usuario = userService.buscarByUsername(username); // Buscar al usuario por nombre

    if (usuario == null) {
        model.addAttribute("error", "Usuario no encontrado.");
        return "redirect:/login"; // Redirigir al login si el usuario no se encuentra
    }

    // Obtener los items del carrito
    List<CarritoItem> itemsCarrito = carritoService.obtenerListaCarrito(usuario);

    if (itemsCarrito.isEmpty()) {
        model.addAttribute("error", "El carrito está vacío.");
        return "redirect:/carrito/lista"; // Redirigir si el carrito está vacío
    }

    // Desvincular los detalles del carrito antes de proceder con la compra
    detallePedidoService.desvincularDetallesDelCarrito(itemsCarrito);

    // Crear los detalles del pedido
    List<DetallePedido> detallesPedido = detallePedidoService.crearDetallePedido(itemsCarrito, tipoEntrega);

    // Guardar los detalles del pedido en la base de datos
    detallePedidoService.guardarDetallesPedido(detallesPedido);

    // Limpiar el carrito después de finalizar la compra (sin eliminar los CarritoItems)
    carritoService.limpiarCarrito(usuario); // Limpiar el carrito del usuario

    // Pasar los detalles del pedido y tipo de entrega a la vista
    model.addAttribute("detallesPedido", detallesPedido);
    model.addAttribute("tipoEntrega", tipoEntrega);

    return "detalles-pedidos";  // Vista para mostrar los detalles de la compra
}



    @GetMapping("/detallePedido")
    public String mostrarDetallePedido() {
        return "detalles-pedidos";
    }
    
    


}
