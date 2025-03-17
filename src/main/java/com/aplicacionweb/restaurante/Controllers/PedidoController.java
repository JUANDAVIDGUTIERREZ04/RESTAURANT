package com.aplicacionweb.restaurante.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.aplicacionweb.restaurante.Models.Pedido;
import com.aplicacionweb.restaurante.Service.PedidoService;

@Controller
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Mostrar la lista de pedidos
    @GetMapping("/listaPedidos")
    public String listarPedidos(Model model) {
        // Obtener la lista de pedidos
        List<Pedido> listaPedidos = pedidoService.obtenerListaPedidos();
        
        // Agregar la lista de pedidos al modelo
        model.addAttribute("pedidos", listaPedidos);
        
        // Retornar la vista donde se mostrarán los pedidos
        return "pedido_lista";  // El nombre de la vista que mostrará los pedidos
    }

    // Eliminar un pedido
    @PostMapping("/pedidos/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        // Llamar al servicio para eliminar el pedido
        pedidoService.eliminarPedido(id);
        
        // Redirigir a la lista de pedidos después de eliminar
        return "redirect:/listaPedidos";
    }

    // Endpoint para cambiar el estado de un pedido
    @PostMapping("/cambiarEstado/{id}")
public String cambiarEstado(@PathVariable Long id) {
    Pedido pedido = pedidoService.obtenerPedidoPorId(id);
    if (pedido != null) {
        if ("Pendiente".equals(pedido.getEstado())) {
            pedido.setEstado("Entregado");
        } else {
            pedido.setEstado("Pendiente");
        }
        pedidoService.guardarPedido(pedido); // Guardar el cambio de estado
    }
    return "redirect:/listaPedidos"; // Redirige a la lista de pedidos después de cambiar el estado
}
}
