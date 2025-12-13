package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.contracts.DtoMapper;
import com.techlab.contracts.dtos.ProductoDto;
import com.techlab.models.productos.Producto;
import com.techlab.services.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

  private final IProductoService productoService;

  public ProductoController(IProductoService productoService) {
    this.productoService = productoService;
  }

  @Operation(summary = "List all products")
  @GetMapping
  public ResponseEntity<Result<List<ProductoDto>>> listar() {
    List<Producto> productos = productoService.listarProductos();
    List<ProductoDto> dtos = productos.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(dtos));
  }

  @Operation(summary = "Search products by name")
  @GetMapping("/buscar")
  public ResponseEntity<Result<List<ProductoDto>>> buscarPorNombre(
      @RequestParam(value = "nombre", required = false) String nombre) {
    List<Producto> productos = productoService.buscarPorNombre(nombre);
    List<ProductoDto> dtos = productos.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(dtos));
  }

  @Operation(summary = "List products by category")
  @GetMapping("/categoria/{categoriaId}")
  public ResponseEntity<Result<List<ProductoDto>>> listarPorCategoria(@PathVariable Long categoriaId) {
    List<Producto> productos = productoService.listarPorCategoria(categoriaId);
    List<ProductoDto> dtos = productos.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(dtos));
  }

  @Operation(summary = "Create a new product")
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<ProductoDto>> crear(@Valid @RequestBody ProductoDto productoDto) {
    Producto producto = DtoMapper.fromDto(productoDto);
    Producto creado = productoService.crearProducto(producto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Result.success("Producto creado exitosamente", DtoMapper.toDto(creado)));
  }

  @Operation(summary = "Get a product by ID")
  @GetMapping("/{id}")
  public ResponseEntity<Result<ProductoDto>> obtener(@PathVariable Long id) {
    Producto producto = productoService.obtenerProductoPorId(id);
    return ResponseEntity.ok(Result.success(DtoMapper.toDto(producto)));
  }

  @Operation(summary = "Update a product by ID")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<ProductoDto>> actualizar(@PathVariable Long id,
      @Valid @RequestBody ProductoDto productoDto) {
    Producto producto = DtoMapper.fromDto(productoDto);
    Producto actualizado = productoService.actualizarProducto(id, producto);
    return ResponseEntity.ok(Result.success("Producto actualizado exitosamente", DtoMapper.toDto(actualizado)));
  }

  @Operation(summary = "Physically delete a product by ID")
  @DeleteMapping("/{id}/fisico")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<Void>> eliminarFisico(@PathVariable Long id) {
    productoService.eliminarFisicamente(id);
    return ResponseEntity.ok(Result.success("Producto eliminado físicamente", null));
  }

  @Operation(summary = "Logically delete a product by ID (alternative)")
  @DeleteMapping("/{id}/logico")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<Void>> eliminarLogico(@PathVariable Long id) {
    productoService.eliminarLogicamente(id);
    return ResponseEntity.ok(Result.success("Producto eliminado lógicamente", null));
  }
}
