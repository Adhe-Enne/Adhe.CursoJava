package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.contracts.DtoMapper;
import com.techlab.contracts.dtos.ProductoDto;
import com.techlab.models.productos.Producto;
import com.techlab.services.IProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

  private final IProductoService productoService;

  public ProductoController(IProductoService productoService) {
    this.productoService = productoService;
  }

  @GetMapping
  public ResponseEntity<Result<java.util.List<ProductoDto>>> listar() {
    try {
      java.util.List<Producto> productos = productoService.listarProductos();
      java.util.List<ProductoDto> dtos = productos.stream().map(DtoMapper::toDto).toList();
      return ResponseEntity.ok(Result.success(dtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al listar productos: " + e.getMessage()));
    }
  }

  @PostMapping
  public ResponseEntity<Result<ProductoDto>> crear(@Valid @RequestBody ProductoDto productoDto) {
    try {
      Producto producto = DtoMapper.fromDto(productoDto);
      Producto creado = productoService.crearProducto(producto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Result.success("Producto creado exitosamente", DtoMapper.toDto(creado)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al crear producto: " + e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<ProductoDto>> obtener(@PathVariable Long id) {
    try {
      Producto producto = productoService.obtenerProductoPorId(id);
      return ResponseEntity.ok(Result.success(DtoMapper.toDto(producto)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Result.failure("Producto no encontrado: " + e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Result<ProductoDto>> actualizar(@PathVariable Long id,
      @Valid @RequestBody ProductoDto productoDto) {
    try {
      Producto producto = DtoMapper.fromDto(productoDto);
      Producto actualizado = productoService.actualizarProducto(id, producto);
      return ResponseEntity.ok(Result.success("Producto actualizado exitosamente", DtoMapper.toDto(actualizado)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al actualizar producto: " + e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> eliminar(@PathVariable Long id) {
    try {
      productoService.eliminarProducto(id);
      return ResponseEntity.ok(Result.success("Producto eliminado exitosamente", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al eliminar producto: " + e.getMessage()));
    }
  }
}
