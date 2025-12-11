package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.contracts.DtoMapper;
import com.techlab.contracts.dtos.CategoriaDto;
import com.techlab.models.categorias.Categoria;
import com.techlab.services.ICategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

  private final ICategoriaService categoriaService;

  public CategoriaController(ICategoriaService categoriaService) {
    this.categoriaService = categoriaService;
  }

  @GetMapping
  public ResponseEntity<Result<java.util.List<CategoriaDto>>> listar() {
    try {
      java.util.List<Categoria> categorias = categoriaService.listarCategorias();
      java.util.List<CategoriaDto> dtos = categorias.stream().map(DtoMapper::toDto).toList();
      return ResponseEntity.ok(Result.success(dtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al listar categorías: " + e.getMessage()));
    }
  }

  @PostMapping
  public ResponseEntity<Result<CategoriaDto>> crear(@Valid @RequestBody CategoriaDto categoriaDto) {
    try {
      Categoria c = DtoMapper.fromDto(categoriaDto);
      Categoria creado = categoriaService.crearCategoria(c);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Result.success("Categoría creada exitosamente", DtoMapper.toDto(creado)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al crear categoría: " + e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<CategoriaDto>> obtener(@PathVariable Long id) {
    try {
      Categoria c = categoriaService.obtenerCategoriaPorId(id);
      return ResponseEntity.ok(Result.success(DtoMapper.toDto(c)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.failure(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al obtener categoría: " + e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Result<CategoriaDto>> actualizar(@PathVariable Long id,
      @Valid @RequestBody CategoriaDto categoriaDto) {
    try {
      Categoria c = DtoMapper.fromDto(categoriaDto);
      Categoria actualizado = categoriaService.actualizarCategoria(id, c);
      return ResponseEntity.ok(Result.success("Categoría actualizada", DtoMapper.toDto(actualizado)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.failure(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al actualizar categoría: " + e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> eliminar(@PathVariable Long id) {
    try {
      categoriaService.eliminarCategoria(id);
      return ResponseEntity.ok(Result.success("Categoría eliminada", null));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.failure(e.getMessage()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.failure(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al eliminar categoría: " + e.getMessage()));
    }
  }
}
