package com.techlab.services;

import java.util.List;

import com.techlab.models.productos.Producto;

public interface IProductoService {
  List<Producto> listarProductos();

  Producto obtenerProductoPorId(Long id);

  Producto crearProducto(Producto producto);

  Producto actualizarProducto(Long id, Producto producto);

  void eliminarProducto(Long id);
}
