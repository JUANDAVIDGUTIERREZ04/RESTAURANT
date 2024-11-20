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

    // Obtener los pedidos de un usuario específico
    public List<Pedido> obtenerPedidosPorUsuario(User usuario) {
        return pedidoRepository.findByUsuario(usuario); // Busca pedidos por usuario
    }
}
