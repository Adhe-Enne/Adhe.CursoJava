package com.techlab.managers;

import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Producto;
import com.techlab.services.PedidoService;
import com.techlab.services.ProductoService;
import com.techlab.util.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class PedidoManager {
  private final PedidoService pedidoService;
  private final ProductoService prodService;
  private final Scanner scanner;

  public PedidoManager(Scanner scanner) {
    this.pedidoService = new PedidoService();
    this.prodService = new ProductoService();
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

    while (true) {
      System.out.print("Ingrese el ID o nombre del producto a agregar: ");

      try {
        Producto producto = buscarProducto();
        int cantidad = solicitarCantidadParaProducto(producto);

        if (cantidad == 0) {
          continue;
        }

        seleccion.productos.add(producto);
        seleccion.cantidades.add(cantidad);
      } catch (Exception ex) {
        System.out.println(ex.getMessage() + " Intente nuevamente.");
        continue;
      }

      System.out.print("¿Desea agregar otro producto al pedido? (s/n): ");
      String resp = scanner.nextLine();

      if (!resp.equalsIgnoreCase("s"))
        break;
    }

    return seleccion;
  }

  private Producto buscarProducto() {
    Producto producto = null;
    String input = scanner.nextLine();

    try {
      int id = Integer.parseInt(input);
      producto = prodService.buscarPorId(id).orElse(null);
    } catch (NumberFormatException e) {
      producto = prodService.buscarPorNombre(input).orElse(null);
    }

    if (producto == null) {
      throw new RuntimeException("Producto no encontrado.");
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
        if (cantidad <= 0) {
          System.out.println("La cantidad debe ser mayor a cero.");
          continue;
        }

        if (cantidad > producto.getStock()) {
          System.out.println("Stock insuficiente. Stock disponible: " + producto.getStock());
          System.out.print("¿Desea ingresar otra cantidad? (s/n): ");
          String resp = scanner.nextLine();

          if (resp.equalsIgnoreCase("s"))
            continue;
          else
            return 0;

        } else
          return cantidad;

      } catch (NumberFormatException ex) {
        System.out.println("Ingrese un número válido para la cantidad.");
      }
    }
  }

  private class SeleccionPedido {
    ArrayList<Producto> productos = new ArrayList<>();
    ArrayList<Integer> cantidades = new ArrayList<>();
  }
}