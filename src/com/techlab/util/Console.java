package com.techlab.util;

import java.util.Scanner;

public class Console {
  private static Scanner scanner = new Scanner(System.in);

  // Método para imprimir la línea separadora
  public static void imprimirSeparador() {
    System.out
        .println("==================================================================================================");
  }

  public static void imprimirEncabezado(String titulo) {
    imprimirSeparador();
    System.out.println("\n--- " + titulo + " ---");
    imprimirSeparador();
  }

  public static void imprimirFinProceso(String tituloString) {
    System.out.println(tituloString);
    imprimirSeparador();
    esperarEnter();
  }

  public static void esperarEnter() {
    System.out.print("\nPresione ENTER para volver al menú...");
    scanner.nextLine();
  }

  public static void limpiarPantalla() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (Exception e) {
      // Si falla, simplemente no limpia la pantalla, pero no deberia fallar nunca....
    }
  }

}