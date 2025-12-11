package com.techlab.contracts.dtos;

import jakarta.validation.constraints.*;

public class UsuarioRequest {
  @NotNull
  @Size(min = 2, max = 100)
  private String nombre;

  @NotNull
  @Email
  @Size(max = 150)
  private String email;

  @NotNull
  @Size(min = 6)
  private String password;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
