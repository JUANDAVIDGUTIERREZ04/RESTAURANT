package com.aplicacionweb.restaurante.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aplicacionweb.restaurante.Models.Pedido;
import com.aplicacionweb.restaurante.Models.User;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    List<Pedido> findByUsuario(User usuario);
    
}