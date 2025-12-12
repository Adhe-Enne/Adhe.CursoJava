package com.techlab.services;

import com.techlab.models.pedidos.Pedido;
import java.util.List;

public interface IPedidoService {
  Pedido crearPedido(Pedido pedido);

  List<Pedido> listarPedidosPorUsuario(Long usuarioId);

  List<Pedido> listarPedidos();

  Pedido actualizarEstadoPedido(Long id, String estado);

  void eliminarPedido(Long id);
}
