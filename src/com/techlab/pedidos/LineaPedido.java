package com.techlab.pedidos;

import com.techlab.productos.Producto;

public class LineaPedido {
  private Producto producto;
  private int cantidad;
  private int id;
  private int pedidoId;

  public LineaPedido(Producto producto, int cantidad, int id, int pedidoId) {
    this.producto = producto;
    this.cantidad = cantidad;
    this.id = id;
    this.pedidoId = pedidoId;
  }

  public Producto getProducto() {
    return producto;
  }

  public int getId() {
    return id;
  }

  public int getPedidoId() {
    return pedidoId;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public double getSubtotal() {
    return producto.getPrecio() * cantidad;
  }
}
