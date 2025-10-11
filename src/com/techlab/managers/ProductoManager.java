package com.techlab.managers;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.productos.Producto;
import com.techlab.services.ProductoService;
import com.techlab.util.Console;
import java.util.Scanner;

public class ProductoManager {
  private final ProductoService prodService;
  private final Scanner scanner;

  public ProductoManager(Scanner scanner, ProductoService prodService) {
    this(scanner, prodService, false);
  }

  public ProductoManager(Scanner scanner, ProductoService prodService, boolean precargar) {
    this.prodService = prodService;
    this.scanner = scanner;
    if (precargar) {
      precargarProductos();
    }
  }

  public void agregarProducto(String nombre, float precio, int stock) {
    prodService.agregarProducto(nombre, precio, stock);
  }

  public void agregarProducto() {
    Console.imprimirEncabezado("Agregar Producto");

    System.out.print("Nombre: ");
    String nombre = scanner.nextLine();
    float precio = solicitarPrecio();
    int stock = solicitarStock();

    Producto p = prodService.agregarProducto(nombre, precio, stock);
    Console.imprimirEncabezado("Producto agregado: " + p);
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
      Console.imprimirEncabezado("Producto encontrado: " + producto);

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

  private boolean confirmarActualizacion() {
    System.out.print("¿Desea actualizar este producto? (s/n): ");
    String resp = scanner.nextLine();
    return resp.equalsIgnoreCase("s");
  }

  private float solicitarPrecio() {
    float precio = 0f;
    while (true) {
      System.out.print("Precio: ");
      String input = scanner.nextLine();
      try {
        precio = Float.parseFloat(input);
        if (precio > 0)
          break;

        System.out.println("El precio no puede ser negativo.");
      } catch (NumberFormatException _) {
        System.out.println("Ingrese un valor numérico válido para el precio.");
      }
    }
    return precio;
  }

  private int solicitarStock() {
    int stock = 0;
    while (true) {
      System.out.print("Stock: ");
      String input = scanner.nextLine();
      try {
        stock = Integer.parseInt(input);

        if (stock > 0)
          break;

        System.out.println("El stock no puede ser negativo.");
      } catch (NumberFormatException _) {
        System.out.println("Ingrese un valor entero válido para el stock.");
      }
    }
    return stock;
  }

  private float solicitarNuevoPrecio(float precioActual) {
    System.out.print("Nuevo precio (actual: " + precioActual + "): ");
    float p;
    String inputPrecio;

    while (true) {
      inputPrecio = scanner.nextLine();

      try {
        if (!inputPrecio.isBlank()) {
          p = Float.parseFloat(inputPrecio);

          if (p < 0)
            System.out.println("El precio no puede ser negativo. Ingrese un valor válido.");
          else if (p == 0)
            System.out.println("El precio no puede ser cero. Ingrese un valor válido.");
          else if (p == precioActual)
            System.out.println("El precio es igual al actual. Ingrese un precio diferente.");
          else
            break;
        } else
          System.out.println("El precio no puede estar vacío. Ingrese un valor válido.");
      } catch (NumberFormatException _) {
        System.out.println("Valor inválido. Ingrese un precio válido.");
      }
    }

    return p;
  }

  private int solicitarNuevoStock(int stockActual) {
    System.out.print("Nuevo stock (actual: " + stockActual + "): ");
    int nuevoStock;

    while (true) {
      String inputStock = scanner.nextLine();

      if (inputStock != null && !inputStock.isBlank()) {
        try {
          nuevoStock = Integer.parseInt(inputStock);

          if (nuevoStock == stockActual)
            System.out.println("El stock es igual al actual. Ingrese un stock diferente.");
          else if (nuevoStock == 0)
            System.out.println("El Stock no puede ser cero.");
          else if (nuevoStock < 0)
            System.out.println("El Stock no puede ser negativo.");
          else
            break;
        } catch (NumberFormatException _) {
          System.out.println("Valor inválido. Ingrese un stock válido.");
        }
      } else
        System.out.println("El stock no puede estar vacío. Ingrese un valor válido.");
    }

    return nuevoStock;
  }

  private String solicitarNuevoNombre(String nombreActual) {
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

  public void precargarProductos() {
    agregarProducto("Café Premium", 1500.5f, 10);
    agregarProducto("Té Verde", 1200.0f, 20);
    agregarProducto("Yerba Mate", 900.0f, 15);
    agregarProducto("Azúcar", 500.0f, 30);
    agregarProducto("Leche", 800.0f, 25);
    agregarProducto("Galletas", 600.0f, 40);
    agregarProducto("Mermelada", 1100.0f, 12);
    agregarProducto("Pan Integral", 700.0f, 18);
  }
}