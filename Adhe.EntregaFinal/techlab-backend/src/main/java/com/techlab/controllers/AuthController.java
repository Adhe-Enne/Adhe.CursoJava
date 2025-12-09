package com.techlab.controllers;

import com.techlab.contracts.AuthRequest;
import com.techlab.contracts.Result;
import com.techlab.models.usuarios.Usuario;
import com.techlab.security.JwtUtil;
import com.techlab.services.IUsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final IUsuarioService usuarioService;

  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
      UserDetailsService userDetailsService, IUsuarioService usuarioService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.usuarioService = usuarioService;
  }

  @PostMapping("/login")
  public ResponseEntity<Result<String>> login(@Valid @RequestBody AuthRequest req) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
      final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
      final String token = jwtUtil.generateToken(userDetails.getUsername());
      return ResponseEntity.ok(Result.success("Login exitoso", token));
    } catch (AuthenticationException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.failure("Credenciales inválidas"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Result<Usuario>> register(@Valid @RequestBody Usuario usuario) {
    try {
      Usuario created = usuarioService.crearUsuario(usuario);
      return ResponseEntity.status(HttpStatus.CREATED).body(Result.success("Usuario registrado exitosamente", created));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Result.failure("Error al registrar usuario: " + e.getMessage()));
    }
  }
}
