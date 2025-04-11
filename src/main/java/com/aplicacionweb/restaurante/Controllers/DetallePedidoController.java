package com.aplicacionweb.restaurante.Controllers;
/* 


import com.aplicacionweb.restaurante.Models.DetallePedido;
import com.aplicacionweb.restaurante.Service.DetallePedidoService;
import com.aplicacionweb.restaurante.Models.CarritoItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/detallePedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    // Endpoint para finalizar la compra
    @PostMapping("/finalizar-compra")
    public String finalizarCompra(String tipoEntrega, Model model) {
        // Aquí se debe agregar la lógica de obtener los productos en el carrito
        List<CarritoItem> itemsCarrito = obtenerItemsCarrito();  // Implementar este método para obtener los items

        // Crear un nuevo DetallePedido con la información del carrito
        List<DetallePedido> detallesPedido = detallePedidoService.crearDetallePedido(itemsCarrito, tipoEntrega);

        // Añadir los detalles del pedido y la información del tipo de entrega a la vista
        model.addAttribute("detallesPedido", detallesPedido);
        model.addAttribute("tipoEntrega", tipoEntrega);

        return "detalles-pedido"; // Nombre de la vista donde se muestran los detalles
    }

    // Método auxiliar para obtener los items del carrito (deberías implementarlo correctamente)
    private List<CarritoItem> obtenerItemsCarrito() {
        // Aquí debes obtener los items del carrito, por ejemplo, desde la sesión del usuario.
        return null;  // Este método es solo un placeholder
    }
}
*/