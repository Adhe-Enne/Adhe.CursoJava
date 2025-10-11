package com.techlab.util;

import java.util.Scanner;

public class Console {
  private static Scanner scanner = new Scanner(System.in);

  private Console() {
    super();
  }

  public static Console fluent() {
    return new Console();
  }

  public Console addLine(String message) {
    System.out.println(message);
    return this;
  }

  public static void coutln(String message) {
    System.out.println(message);
  }

  public static void cout(String message) {
    System.out.print(message);
  }

  public static void cout(Exception exception) {
    System.out.print(exception.getMessage());
  }

  public static void imprimirSeparador() {
    System.out
        .println("==================================================================================================");
  }

  public static void imprimirEncabezado(String titulo) {
    imprimirSeparador();
    System.out.println("--- " + titulo + " ---");
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
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      cout(e);
    } catch (Exception e) {
      // Si falla, simplemente no limpia la pantalla, pero no deberia fallar nunca....
      cout(e);
    }
  }
}