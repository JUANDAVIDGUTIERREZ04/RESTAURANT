package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Pedido;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Guardar un nuevo pedido
    public void guardarPedido(Pedido pedido) {
        pedidoRepository.save(pedido); // Guarda el pedido en la base de datos
    }

    // Obtener todos los pedidos
    public List<Pedido> obtenerListaPedidos() {
        return pedidoRepository.findAll(); // Devuelve la lista completa de pedidos
    }

    // Eliminar un pedido por ID
    public void eliminarPedido(Long idPedido) {
        pedidoRepository.deleteById(idPedido); // Elimina el pedido con el ID proporcionado
    }
    
    //estado  
    public void cambiarEstado(Long idPedido) {
        // Buscar el pedido por su ID
        Pedido pedido = pedidoRepository.findById(idPedido).orElse(null);

        if (pedido != null) {
            // Alternar el estado entre 'Pendiente' y 'Entregado'
            if ("Pendiente".equals(pedido.getEstado())) {
                pedido.setEstado("Entregado");
            } else if ("Entregado".equals(pedido.getEstado())) {
                pedido.setEstado("Pendiente");
            }
            
            // Guardar el cambio en la base de datos
            pedidoRepository.save(pedido);
        }
    }

    // Obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido).orElse(null); // Devuelve el pedido si existe, o null si no se encuentra
    }

    // Obtener los pedidos de un usuario específico
    public List<Pedido> obtenerPedidosPorUsuario(User usuario) {
        return pedidoRepository.findByUsuario(usuario); // Busca pedidos por usuario
    }
}
