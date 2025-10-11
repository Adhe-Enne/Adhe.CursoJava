package com.techlab.managers;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.pedidos.SeleccionPedido;
import com.techlab.productos.Producto;
import com.techlab.services.PedidoService;
import com.techlab.services.ProductoService;
import com.techlab.util.Console;
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
    SeleccionPedido seleccion = seleccionarProductosParaPedido();

    if (seleccion.productos.isEmpty()) {
      Console.imprimirFinProceso("No se seleccionaron productos para el pedido.");
      return;
    }

    Console.limpiarPantalla();
    Console.imprimirEncabezado("Resumen de productos seleccionados para el pedido");
    float total = 0f;

    for (int i = 0; i < seleccion.productos.size(); i++) {
      Producto p = seleccion.productos.get(i);
      int cant = seleccion.cantidades.get(i);
      System.out.println(p + " | Cantidad: " + cant + " | Subtotal: $" + (p.getPrecio() * cant));
      total += p.getPrecio() * cant;
    }

    Console.imprimirEncabezado("Total del pedido: $" + total);

    System.out.print("¿Confirmar pedido? (s/n): ");
    String confirmar = scanner.nextLine();

    if (!confirmar.equalsIgnoreCase("s")) {
      Console.imprimirFinProceso("Pedido cancelado.");
      return;
    }

    Pedido pedido = pedidoService.crearPedido();

    for (int i = 0; i < seleccion.productos.size(); i++) {
      Producto p = seleccion.productos.get(i);
      int cant = seleccion.cantidades.get(i);
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
      System.out.println("\nPedido ID: " + pedido.getId());
      float total = 0f;

      for (LineaPedido linea : pedido.getLineas()) {
        float subtotal = linea.getProducto().getPrecio() * linea.getCantidad();
        total += subtotal;
        System.out.println("  - " + linea.getProducto().getNombre() + " | Cantidad: " + linea.getCantidad()
            + " | Subtotal: $" + subtotal);
      }
      System.out.println("  Total: $" + total);
      Console.imprimirSeparador();
    }

    Console.esperarEnter();
  }

  private SeleccionPedido seleccionarProductosParaPedido() {
    SeleccionPedido seleccion = new SeleccionPedido();
    System.out.println("\nProductos disponibles:");

    prodService.getProductos().forEach(System.out::println);

    boolean seguir = true;
    while (seguir) {
      System.out.print("Ingrese el ID o nombre del producto a agregar: ");

      try {
        Producto producto = buscarProducto();
        int cantidad = solicitarCantidadParaProducto(producto);

        seleccion.productos.add(producto);
        seleccion.cantidades.add(cantidad);
      } catch (Exception ex) {
        System.out.println(ex.getMessage() + " Intente nuevamente.");
        continue;
      }

      System.out.print("¿Desea agregar otro producto al pedido? (s/n): ");
      String resp = scanner.nextLine();

      if (!resp.equalsIgnoreCase("s"))
        seguir = false;
    }

    return seleccion;
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

    if (producto == null) {
      throw new ProductoNoEncontradoException("Producto no encontrado.");
    }

    return producto;
  }

  private int solicitarCantidadParaProducto(Producto producto) {
    int cantidad = 0;

    while (true) {
      System.out.print("Cantidad a agregar: ");
      String cantInput = scanner.nextLine();

      try {
        cantidad = Integer.parseInt(cantInput);

        if (cantidad <= 0)
          System.out.println("La cantidad debe ser mayor a cero.");

        if (cantidad > producto.getStock()) {
          System.out.println("Stock insuficiente. Stock disponible: " + producto.getStock());
          System.out.print("Ingrese una cantidad válida (1-" + producto.getStock() + "): ");
        }

        if (cantidad > 0 && cantidad <= producto.getStock())
          break;
      } catch (NumberFormatException _) {
        System.out.println("Ingrese un número válido para la cantidad.");
      }
    }
    return cantidad;
  }
}