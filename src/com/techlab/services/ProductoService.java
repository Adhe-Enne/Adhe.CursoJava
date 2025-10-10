package com.techlab.services;

import com.techlab.productos.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {
  private ArrayList<Producto> productos = new ArrayList<Producto>();
  private static int nextId = 1;

  /**
   * Agrega un nuevo producto al sistema.
   *
   * @param nombre
   *          El nombre del producto.
   * @param precio
   *          El precio del producto.
   * @param stock
   *          La cantidad en stock del producto.
   * @return El producto creado.
   */
  public Producto agregarProducto(String nombre, float precio, int stock) {
    Producto p = new Producto(nextId++, nombre, precio, stock);
    productos.add(p);
    return p;
  }

  public List<Producto> getProductos() {
    return productos;
  }

  public Optional<Producto> buscarPorId(int id) {
    return productos.stream().filter(p -> p.getId() == id).findFirst();
  }

  public Optional<Producto> buscarPorNombre(String nombre) {

    return productos.stream()
        .filter(
            p -> p.getNombre().equalsIgnoreCase(nombre) || p.getNombre().toLowerCase().startsWith(nombre.toLowerCase()))
        .findFirst();
  }

  public boolean eliminarProducto(int id) {
    return productos.removeIf(p -> p.getId() == id);
  }
}
