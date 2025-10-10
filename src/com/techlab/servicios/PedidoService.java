package com.techlab.servicios;

import com.techlab.pedidos.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
  private List<Pedido> pedidos = new ArrayList<Pedido>();
  private int nextId = 1;

  public Pedido crearPedido() {
    Pedido pedido = new Pedido(nextId++);
    pedidos.add(pedido);
    return pedido;
  }

  public List<Pedido> getPedidos() {
    return pedidos;
  }
}
