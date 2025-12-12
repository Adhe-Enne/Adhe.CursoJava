package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.contracts.DtoMapper;
import com.techlab.contracts.dtos.PedidoRequest;
import com.techlab.contracts.dtos.PedidoResponse;
import com.techlab.models.pedidos.Pedido;
import com.techlab.services.IPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class PedidoController {

  private final IPedidoService pedidoService;

  public PedidoController(IPedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @Operation(summary = "Create a new order")
  @PostMapping("/pedidos")
  public ResponseEntity<Result<PedidoResponse>> crearPedido(@Valid @RequestBody PedidoRequest pedidoReq) {
    Pedido pedido = DtoMapper.fromRequest(pedidoReq);
    Pedido creado = pedidoService.crearPedido(pedido);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Result.success("Pedido creado exitosamente", DtoMapper.toDto(creado)));
  }

  @Operation(summary = "List orders by user ID")
  @GetMapping("/usuarios/{id}/pedidos")
  public ResponseEntity<Result<java.util.List<PedidoResponse>>> listarPorUsuario(@PathVariable("id") Long usuarioId) {
    java.util.List<Pedido> pedidos = pedidoService.listarPedidosPorUsuario(usuarioId);
    java.util.List<PedidoResponse> resp = pedidos.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(resp));
  }

  @Operation(summary = "List all orders")
  @GetMapping("/pedidos")
  public ResponseEntity<Result<java.util.List<PedidoResponse>>> listarPedidos() {
    java.util.List<Pedido> pedidos = pedidoService.listarPedidos();
    java.util.List<PedidoResponse> resp = pedidos.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(resp));
  }

  @Operation(summary = "Update order status")
  @PutMapping("/pedidos/{id}/estado")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<PedidoResponse>> actualizarEstado(@PathVariable Long id,
      @RequestBody java.util.Map<String, String> body) {
    String estado = body.get("estado");
    Pedido actualizado = pedidoService.actualizarEstadoPedido(id, estado);
    return ResponseEntity.ok(Result.success("Estado actualizado exitosamente", DtoMapper.toDto(actualizado)));
  }

  @Operation(summary = "Delete an order")
  @DeleteMapping("/pedidos/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<Void>> eliminarPedido(@PathVariable Long id) {
    pedidoService.eliminarPedido(id);
    return ResponseEntity.ok(Result.success("Pedido eliminado exitosamente", null));
  }
}
