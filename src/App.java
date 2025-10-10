
import java.util.Scanner;

import com.techlab.servicios.ManagerService;
import com.techlab.util.Console;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ManagerService manager = new ManagerService(scanner);
        manager.precargarProductos(); // Cargar algunos productos de ejemplo

        int opcion;

        label: while (true) {
            Console.imprimirSeparador();
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine();
            Console.imprimirSeparador();
            Console.limpiarPantalla();

            switch (opcion) {
                case 1 -> manager.agregarProducto();
                case 2 -> manager.listarProductos();
                case 3 -> manager.buscarActualizarProducto();
                case 4 -> manager.eliminarProducto();
                case 5 -> manager.crearPedido();
                case 6 -> manager.listarPedidos();
                case 7 -> {
                    // Separador antes de salir
                    Console.imprimirEncabezado("Saliendo del sistema. ¡Hasta luego!");
                    break label;
                }
                default -> {
                    System.out.println("Opción inválida. Intente nuevamente.");
                    Console.imprimirSeparador();
                }
            }
            Console.limpiarPantalla();
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println(
                "\n=================================== SISTEMA DE GESTIÓN - TECHLAB ==================================");
        System.out.println("\n1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.println(
                "==================================================================================================");
        System.out.print("\nElija una opción: ");
    }

}
