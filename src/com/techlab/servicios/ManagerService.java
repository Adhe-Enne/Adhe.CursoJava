package com.techlab.servicios;

import com.techlab.excepciones.ProductoNoEncontradoException;
import java.util.ArrayList;
import java.util.Scanner;
import com.techlab.productos.Producto;
import com.techlab.util.Console;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;

public class ManagerService {
  private final ProductoService prodService = new ProductoService();
  private final PedidoService pedidoService = new PedidoService();
  private Scanner scanner;

  public ManagerService(Scanner scanner) {
    super();
    this.scanner = scanner;
  }

  // Emular datos
  public void precargarProductos() {
    prodService.agregarProducto("Café Premium", 1500.5f, 10);
    prodService.agregarProducto("Té Verde", 1200.0f, 20);
    prodService.agregarProducto("Yerba Mate", 900.0f, 15);
    prodService.agregarProducto("Azúcar", 500.0f, 30);
    prodService.agregarProducto("Leche", 800.0f, 25);
    prodService.agregarProducto("Galletas", 600.0f, 40);
    prodService.agregarProducto("Mermelada", 1100.0f, 12);
    prodService.agregarProducto("Pan Integral", 700.0f, 18);
  }

  // Métodos a implementar
  public void agregarProducto() {
    Console.imprimirEncabezado("Agregar Producto");

    System.out.print("Nombre: ");
    String nombre = scanner.nextLine();
    float precio = 0f;
    int stock = 0;

    // Validar precio
    while (true) {
      System.out.print("Precio: ");
      String input = scanner.nextLine();
      try {
        precio = Float.parseFloat(input);
        if (precio > 0)
          break;

        System.out.println("El precio no puede ser negativo.");
      } catch (NumberFormatException e) {
        System.out.println("Ingrese un valor numérico válido para el precio.");
      }
    }

    while (true) {
      System.out.print("Stock: ");
      String input = scanner.nextLine();
      try {
        stock = Integer.parseInt(input);

        if (stock > 0)
          break;

        System.out.println("El stock no puede ser negativo.");
      } catch (NumberFormatException e) {
        System.out.println("Ingrese un valor entero válido para el stock.");
      }
    }

    Producto p = prodService.agregarProducto(nombre, precio, stock);

    Console.imprimirEncabezado("Producto agregado: " + p);

    // imprimirSeparador();
    Console.esperarEnter();
  }

  public void listarProductos() {
    Console.imprimirEncabezado("Lista de Productos");

    if (prodService.getProductos().isEmpty()) {
      System.out.println("No hay productos registrados.");
    } else {
      prodService.getProductos().forEach(System.out::println);
    }

    Console.imprimirSeparador();
    Console.esperarEnter();
  }

  public void buscarActualizarProducto() {
    Console.imprimirEncabezado("Buscar/Actualizar Producto");

    try {
      System.out.print("Ingrese el ID o el nombre del producto: ");
      Producto producto = buscarProducto();
      Console.imprimirEncabezado("\nProducto encontrado: " + producto);

      if (!confirmarActualizacion()) {
        Console.imprimirFinProceso("No se realizaron cambios.");
        return;
      }

      float nuevoPrecio = solicitarNuevoPrecio(producto.getPrecio());
      producto.setPrecio(nuevoPrecio);

      int nuevoStock = solicitarNuevoStock(producto.getStock());
      producto.setStock(nuevoStock);

      System.out.print("¿Desea cambiar el nombre del producto? (s/n): ");
      String cambiarNombre = scanner.nextLine();

      if (cambiarNombre.equalsIgnoreCase("s")) {
        String nuevoNombre = solicitarNuevoNombre(producto.getNombre());
        producto.setNombre(nuevoNombre);
      }

      Console.imprimirSeparador();
      System.out.println("Producto actualizado: " + producto);
    } catch (ProductoNoEncontradoException ex) {
      System.out.println(ex.getMessage());
    }
    Console.imprimirSeparador();
    Console.esperarEnter();
  }

  private boolean confirmarActualizacion() {
    System.out.print("¿Desea actualizar este producto? (s/n): ");
    String resp = scanner.nextLine();
    return resp.equalsIgnoreCase("s");
  }

  private float solicitarNuevoPrecio(float precioActual) {
    System.out.print("Nuevo precio (actual: " + precioActual + "): ");
    float p;

    while (true) {
      String inputPrecio = scanner.nextLine();
      if (inputPrecio.isBlank()) {
        System.out.println("El precio no puede estar vacío. Ingrese un valor válido.");
        continue;
      }

      try {
        p = Float.parseFloat(inputPrecio);
      } catch (NumberFormatException e) {
        System.out.println("Valor inválido. Ingrese un precio válido.");
        continue;
      }

      if (p < 0) {
        System.out.println("El precio no puede ser negativo. Ingrese un valor válido.");
      } else if (p == 0) {
        System.out.println("El precio no puede ser cero. Ingrese un valor válido.");
      } else if (p == precioActual) {
        System.out.println("El precio es igual al actual. Ingrese un precio diferente.");
      } else {
        break;
      }
    }
    return p;
  }

  private int solicitarNuevoStock(int stockActual) {
    System.out.print("Nuevo stock (actual: " + stockActual + "): ");
    int s;
    while (true) {
      String inputStock = scanner.nextLine();

      if (inputStock.isBlank()) {
        System.out.println("El stock no puede estar vacío. Ingrese un valor válido.");
        continue;
      }

      try {
        s = Integer.parseInt(inputStock);
      } catch (NumberFormatException e) {
        System.out.println("Valor inválido. Ingrese un stock válido.");
        continue;
      }

      if (s == stockActual) {
        System.out.println("El stock es igual al actual. Ingrese un stock diferente.");
        continue;
      } else if (s == 0) {
        System.out.println("El Stock no puede ser cero.");
        continue;
      } else if (s < 0) {
        System.out.println("El Stock no puede ser negativo.");
        continue;
      } else {
        break;
      }

    }
    return s;
  }

  private String solicitarNuevoNombre(String nombreActual) {
    // Cambiar nombre
    String nombre;
    System.out.print("Nuevo nombre (actual: " + nombreActual + "): ");

    while (true) {
      nombre = scanner.nextLine();

      if (nombre.isBlank()) {
        System.out.println("El nombre no puede estar vacío.");
      } else if (nombre.equalsIgnoreCase(nombreActual)) {
        System.out
            .println("El nombre es igual al actual. Ingrese un nombre diferente o deje vacío para mantener el actual.");
      } else if (prodService.buscarPorNombre(nombre).isPresent()) {
        System.out.println("Ya existe un producto con ese nombre. Ingrese un nombre diferente.");
      } else
        break;
    }

    return nombre;
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
      throw new ProductoNoEncontradoException("Producto no encontrado.");
    }

    return producto;
  }

  public void eliminarProducto() {
    Console.imprimirEncabezado("Eliminar Producto");

    try {
      System.out.print("Ingrese el ID o el nombre del producto a eliminar: ");
      Producto producto = buscarProducto();

      Console.imprimirEncabezado("Producto encontrado: " + producto);
      System.out.print("¿Está seguro que desea eliminar este producto? (s/n): ");

      String resp = scanner.nextLine();

      if (!resp.equalsIgnoreCase("s")) {
        Console.imprimirFinProceso("No se realizó ninguna acción.");
        return;
      }

      boolean eliminado = prodService.eliminarProducto(producto.getId());

      if (eliminado) {
        System.out.println("Producto eliminado correctamente.");
      } else {
        System.out.println("No se pudo eliminar el producto.");
      }
    } catch (ProductoNoEncontradoException ex) {
      System.out.println(ex.getMessage());
    }

    Console.imprimirSeparador();
    Console.esperarEnter();
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

    // Descontar stock y crear pedido
    Pedido pedido = pedidoService.crearPedido();

    for (int i = 0; i < seleccion.productos.size(); i++) {
      Producto p = seleccion.productos.get(i);
      int cant = seleccion.cantidades.get(i);
      p.setStock(p.getStock() - cant);
      pedido.agregarLinea(new com.techlab.pedidos.LineaPedido(p, cant, 0, pedido.getId()));
    }

    Console.imprimirEncabezado("Pedido creado exitosamente. ID: " + pedido.getId());
    Console.esperarEnter();
  }

  // Estructura auxiliar para devolver dos listas
  private class SeleccionPedido {
    ArrayList<Producto> productos = new ArrayList<>();
    ArrayList<Integer> cantidades = new ArrayList<>();
  }

  // Método para seleccionar productos y cantidades para el pedido
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
          // Usuario decidió no agregar el producto
          continue;
        }

        seleccion.productos.add(producto);
        seleccion.cantidades.add(cantidad);
      } catch (ProductoNoEncontradoException ex) {
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

  // Método para solicitar cantidad válida para un producto
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
            return 0; // No agregar el producto

        } else
          return cantidad;

      } catch (NumberFormatException ex) {
        System.out.println("Ingrese un número válido para la cantidad.");
      }
    }
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
}