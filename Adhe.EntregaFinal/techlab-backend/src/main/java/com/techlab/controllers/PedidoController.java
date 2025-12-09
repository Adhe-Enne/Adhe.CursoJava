package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.models.pedidos.Pedido;
import com.techlab.services.IPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PedidoController {

  private final IPedidoService pedidoService;

  public PedidoController(IPedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @PostMapping("/pedidos")
  public ResponseEntity<Result<Pedido>> crearPedido(@RequestBody Pedido pedido) {
    try {
      Pedido creado = pedidoService.crearPedido(pedido);
      return ResponseEntity.status(HttpStatus.CREATED).body(Result.success("Pedido creado exitosamente", creado));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al crear el pedido: " + e.getMessage()));
    }
  }

  @GetMapping("/usuarios/{id}/pedidos")
  public ResponseEntity<Result<List<Pedido>>> listarPorUsuario(@PathVariable("id") Long usuarioId) {
    try {
      List<Pedido> pedidos = pedidoService.listarPedidosPorUsuario(usuarioId);
      return ResponseEntity.ok(Result.success(pedidos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al listar pedidos: " + e.getMessage()));
    }
  }

  @PutMapping("/pedidos/{id}/estado")
  public ResponseEntity<Result<Pedido>> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
    try {
      String estado = body.get("estado");
      Pedido actualizado = pedidoService.actualizarEstadoPedido(id, estado);
      return ResponseEntity.ok(Result.success("Estado actualizado exitosamente", actualizado));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al actualizar el estado: " + e.getMessage()));
    }
  }
}
