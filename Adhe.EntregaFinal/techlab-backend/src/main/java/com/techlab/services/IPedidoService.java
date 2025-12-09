package com.techlab.services;

import com.techlab.models.pedidos.Pedido;
import java.util.List;

public interface IPedidoService {
  Pedido crearPedido(Pedido pedido);

  List<Pedido> listarPedidosPorUsuario(Long usuarioId);

  Pedido actualizarEstadoPedido(Long id, String estado);
}
