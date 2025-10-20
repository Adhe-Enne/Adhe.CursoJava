
package com.techlab.managers;

import com.techlab.services.ProductoService;
import com.techlab.services.PedidoService;
import java.util.Scanner;

public class BusinessManager {
  private final ProductoManager productoManager;
  private final PedidoManager pedidoManager;

  public BusinessManager(Scanner scanner) {
    this(scanner, false);
  }

  public BusinessManager(Scanner scanner, boolean stockPrecargado) {
    ProductoService productoService = new ProductoService();
    PedidoService pedidoService = new PedidoService();

    this.productoManager = new ProductoManager(scanner, productoService);
    this.pedidoManager = new PedidoManager(scanner, pedidoService, productoService);

    if (stockPrecargado) {
      precargarProductos();
    }
  }

  public void agregarProducto() {
    productoManager.agregarProducto();
  }

  public void listarProductos() {
    productoManager.listarProductos();
  }

  public void buscarActualizarProducto() {
    productoManager.buscarActualizarProducto();
  }

  public void eliminarProducto() {
    productoManager.eliminarProducto();
  }

  public void crearPedido() {
    pedidoManager.crearPedido();
  }

  public void listarPedidos() {
    pedidoManager.listarPedidos();
  }

  private void precargarProductos() {
    productoManager.agregarProducto("Café Premium", 1500.5f, 10);
    productoManager.agregarProducto("Té Verde", 1200.0f, 20);
    productoManager.agregarProducto("Yerba Mate", 900.0f, 15);
    productoManager.agregarProducto("Azúcar", 500.0f, 30);
    productoManager.agregarProducto("Leche", 800.0f, 25);
    productoManager.agregarProducto("Galletas", 600.0f, 40);
    productoManager.agregarProducto("Mermelada", 1100.0f, 12);
    productoManager.agregarProducto("Pan Integral", 700.0f, 18);
  }
}