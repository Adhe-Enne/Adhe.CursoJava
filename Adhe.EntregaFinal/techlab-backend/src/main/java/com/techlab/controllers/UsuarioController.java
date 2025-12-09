package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.models.usuarios.Usuario;
import com.techlab.services.IUsuarioService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

  private final IUsuarioService usuarioService;

  public UsuarioController(IUsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping
  public ResponseEntity<Result<Usuario>> crearUsuario(@Valid @RequestBody Usuario usuario) {
    try {
      Usuario creado = usuarioService.crearUsuario(usuario);
      return ResponseEntity.status(HttpStatus.CREATED).body(Result.success("Usuario creado exitosamente", creado));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al crear usuario: " + e.getMessage()));
    }
  }

  @GetMapping
  public ResponseEntity<Result<List<Usuario>>> listarUsuarios() {
    try {
      List<Usuario> usuarios = usuarioService.listarUsuarios();
      return ResponseEntity.ok(Result.success(usuarios));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al listar usuarios: " + e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<Usuario>> obtenerUsuario(@PathVariable Long id) {
    try {
      Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
      return ResponseEntity.ok(Result.success(usuario));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Result.failure("Usuario no encontrado: " + e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Usuario>> eliminarLogicamente(@PathVariable Long id) {
    try {
      Usuario usuario = usuarioService.eliminarLogicamente(id);
      return ResponseEntity.ok(Result.success("Usuario eliminado lógicamente", usuario));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al eliminar usuario: " + e.getMessage()));
    }
  }

  @DeleteMapping("/{id}/fisico")
  public ResponseEntity<Result<Void>> eliminarFisicamente(@PathVariable Long id) {
    try {
      usuarioService.eliminarFisicamente(id);
      return ResponseEntity.ok(Result.success("Usuario eliminado físicamente", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Result.failure("Error al eliminar usuario físicamente: " + e.getMessage()));
    }
  }
}
