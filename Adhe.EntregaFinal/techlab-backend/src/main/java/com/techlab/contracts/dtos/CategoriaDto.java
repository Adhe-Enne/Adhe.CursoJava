package com.techlab.contracts.dtos;

import jakarta.validation.constraints.*;

public class CategoriaDto {
  private Long id;

  @NotNull
  @Size(min = 2, max = 100)
  private String nombre;

  @Size(max = 255)
  private String descripcion;

  private boolean activo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }
}
