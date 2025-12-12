package com.techlab.controllers;

import com.techlab.contracts.Result;
import com.techlab.contracts.DtoMapper;
import com.techlab.contracts.dtos.UsuarioRequest;
import com.techlab.contracts.dtos.UsuarioResponse;
import com.techlab.models.usuarios.Usuario;
import com.techlab.services.IUsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

  private final IUsuarioService usuarioService;

  public UsuarioController(IUsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @Operation(summary = "Create a new user")
  @PostMapping
  public ResponseEntity<Result<UsuarioResponse>> crearUsuario(@Valid @RequestBody UsuarioRequest usuarioReq) {
    Usuario u = DtoMapper.fromRequest(usuarioReq);
    Usuario creado = usuarioService.crearUsuario(u);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Result.success("Usuario creado exitosamente", DtoMapper.toDto(creado)));
  }

  @Operation(summary = "List all users")
  @GetMapping
  public ResponseEntity<Result<java.util.List<UsuarioResponse>>> listarUsuarios() {
    java.util.List<Usuario> usuarios = usuarioService.listarUsuarios();
    java.util.List<UsuarioResponse> resp = usuarios.stream().map(DtoMapper::toDto).toList();
    return ResponseEntity.ok(Result.success(resp));
  }

  @Operation(summary = "Get a user by ID")
  @GetMapping("/{id}")
  public ResponseEntity<Result<UsuarioResponse>> obtenerUsuario(@PathVariable Long id) {
    Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
    return ResponseEntity.ok(Result.success(DtoMapper.toDto(usuario)));
  }

  @Operation(summary = "Logically delete a user by ID")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<UsuarioResponse>> eliminarLogicamente(@PathVariable Long id) {
    Usuario usuario = usuarioService.eliminarLogicamente(id);
    return ResponseEntity.ok(Result.success("Usuario eliminado lógicamente", DtoMapper.toDto(usuario)));
  }

  @Operation(summary = "Physically delete a user by ID")
  @DeleteMapping("/{id}/fisico")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<Void>> eliminarFisicamente(@PathVariable Long id) {
    usuarioService.eliminarFisicamente(id);
    return ResponseEntity.ok(Result.success("Usuario eliminado físicamente", null));
  }

  @Operation(summary = "Logically delete a user by ID (alternative method)")
  @DeleteMapping("/{id}/logico")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<Void>> eliminarLogico(@PathVariable Long id) {
    usuarioService.eliminarLogicamente(id);
    return ResponseEntity.ok(Result.success("Usuario eliminado lógicamente", null));
  }

  @Operation(summary = "Update a user by ID")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<UsuarioResponse>> actualizarUsuario(@PathVariable Long id,
      @Valid @RequestBody UsuarioRequest usuarioReq) {
    Usuario u = DtoMapper.fromRequest(usuarioReq);
    Usuario actualizado = usuarioService.actualizarUsuario(id, u);
    return ResponseEntity.ok(Result.success("Usuario actualizado exitosamente", DtoMapper.toDto(actualizado)));
  }

  @Operation(summary = "Search for a user by email")
  @GetMapping("/buscar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<UsuarioResponse>> buscarPorEmail(@RequestParam("email") String email) {
    Usuario u = usuarioService.buscarPorEmail(email);
    return ResponseEntity.ok(Result.success(DtoMapper.toDto(u)));
  }

  @Operation(summary = "Change a user's role")
  @PutMapping("/{id}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Result<UsuarioResponse>> cambiarRolUsuario(@PathVariable Long id,
      @RequestParam("role") String nuevoRol) {
    Usuario usuarioActualizado = usuarioService.cambiarRolUsuario(id, nuevoRol);
    return ResponseEntity
        .ok(Result.success("Rol del usuario actualizado exitosamente", DtoMapper.toDto(usuarioActualizado)));
  }
}
