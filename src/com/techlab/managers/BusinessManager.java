
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
    this.productoManager = new ProductoManager(scanner, productoService, stockPrecargado);
    this.pedidoManager = new PedidoManager(scanner, pedidoService, productoService);
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
}