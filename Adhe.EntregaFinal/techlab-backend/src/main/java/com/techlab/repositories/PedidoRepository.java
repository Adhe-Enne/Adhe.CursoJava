package com.techlab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.models.pedidos.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
  List<Pedido> findByUsuarioId(Long usuarioId);
}
