package com.techlab.managers;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.pedidos.*;
import com.techlab.productos.Producto;
import com.techlab.services.*;
import com.techlab.util.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PedidoManager {
  private final PedidoService pedidoService;
  private final ProductoService prodService;
  private final Scanner scanner;

  public PedidoManager(Scanner scanner, PedidoService pedidoService, ProductoService prodService) {
    this.pedidoService = pedidoService;
    this.prodService = prodService;
    this.scanner = scanner;
  }

  public void crearPedido() {
    Console.imprimirEncabezado("Crear Pedido");
    List<SeleccionLinea> lineas = seleccionarLineas();

    if (lineas.isEmpty()) {
      Console.imprimirFinProceso("No se seleccionaron productos para el pedido.");
      return;
    }

    Console.limpiarPantalla();
    Console.imprimirEncabezado("Resumen de productos seleccionados para el pedido");
    float total = 0f;

    for (SeleccionLinea linea : lineas) {
      Producto p = linea.getProducto();
      int cant = linea.getCantidad();
      Console.coutln(p + " | Cantidad: " + cant + " | Subtotal: $" + (p.getPrecio() * cant));
      total += p.getPrecio() * cant;
    }

    Console.imprimirEncabezado("Total del pedido: $" + total);

    Console.cout("¿Confirmar pedido? (s/n): ");
    String confirmar = scanner.nextLine();

    if (!confirmar.equalsIgnoreCase("s")) {
      Console.imprimirFinProceso("Pedido cancelado.");
      return;
    }

    Pedido pedido = pedidoService.crearPedido();

    for (SeleccionLinea linea : lineas) {
      Producto p = linea.getProducto();
      int cant = linea.getCantidad();
      p.setStock(p.getStock() - cant);
      pedido.agregarLinea(new LineaPedido(p, cant, 0, pedido.getId()));
    }

    Console.imprimirEncabezado("Pedido creado exitosamente. ID: " + pedido.getId());
    Console.esperarEnter();
  }

  public void listarPedidos() {
    Console.imprimirEncabezado("Lista de Pedidos");

    if (pedidoService.getPedidos().isEmpty()) {
      Console.imprimirFinProceso("No hay pedidos registrados.");
      return;
    }

    for (Pedido pedido : pedidoService.getPedidos()) {
      Console.imprimirSeparador();
      Console.coutln("\nPedido ID: " + pedido.getId());
      float total = 0f;

      for (LineaPedido linea : pedido.getLineas()) {
        float subtotal = linea.getProducto().getPrecio() * linea.getCantidad();
        total += subtotal;
        Console.coutln("  - " + linea.getProducto().getNombre() + " | Cantidad: " + linea.getCantidad()
            + " | Subtotal: $" + subtotal);
      }
      Console.coutln("  Total: $" + total);
      Console.imprimirSeparador();
    }

    Console.esperarEnter();
  }

  private List<SeleccionLinea> seleccionarLineas() {
    List<SeleccionLinea> lineas = new ArrayList<>();

    Console.coutln("\nProductos disponibles:");

    prodService.getProductos().forEach(p -> Console.coutln(p.toString()));

    boolean seguir = true;
    while (seguir) {
      Console.cout("Ingrese el ID o nombre del producto a agregar: ");

      try {
        Producto producto = buscarProducto();
        int cantidad = solicitarCantidadParaProducto(producto);

        lineas.add(new SeleccionLinea(producto, cantidad));
      } catch (Exception ex) {
        Console.coutln(ex.getMessage() + " Intente nuevamente.");
        continue;
      }

      Console.cout("¿Desea agregar otro producto al pedido? (s/n): ");
      String resp = scanner.nextLine();

      if (!resp.equalsIgnoreCase("s"))
        seguir = false;
    }

    return lineas;
  }

  private Producto buscarProducto() {
    Producto producto = null;
    String input = scanner.nextLine();

    try {
      int id = Integer.parseInt(input);
      producto = prodService.buscarPorId(id).orElse(null);
    } catch (NumberFormatException _) {
      producto = prodService.buscarPorNombre(input).orElse(null);
    }

    if (producto == null)
      throw new ProductoNoEncontradoException("Producto no encontrado.");

    return producto;
  }

  private int solicitarCantidadParaProducto(Producto producto) {
    int cantidad = 0;

    while (true) {
      Console.cout("Cantidad a agregar: ");
      String cantInput = scanner.nextLine();

      try {
        cantidad = Integer.parseInt(cantInput);

        if (cantidad <= 0)
          Console.coutln("La cantidad debe ser mayor a cero.");

        if (cantidad > producto.getStock()) {
          Console.coutln("Stock insuficiente. Stock disponible: " + producto.getStock());
          Console.cout("Ingrese una cantidad válida (1-" + producto.getStock() + "): ");
        }

        if (cantidad > 0 && cantidad <= producto.getStock())
          break;
      } catch (NumberFormatException _) {
        Console.coutln("Ingrese un número válido para la cantidad.");
      }
    }
    return cantidad;
  }
}