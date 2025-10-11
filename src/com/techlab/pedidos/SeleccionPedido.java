package com.techlab.pedidos;

import java.util.ArrayList;
import java.util.List;

import com.techlab.productos.Producto;

public class SeleccionPedido {
  public final List<Producto> productos = new ArrayList<>();
  public final List<Integer> cantidades = new ArrayList<>();

  public List<Producto> getProductos() {
    return productos;
  }

  public List<Integer> getCantidades() {
    return cantidades;
  }
}
