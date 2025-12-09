package com.techlab.services.impl;

import com.techlab.models.usuarios.Usuario;
import com.techlab.repositories.UsuarioRepository;
import com.techlab.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<Usuario> listarUsuarios() {
    return usuarioRepository.findAll();
  }

  @Override
  public Usuario crearUsuario(Usuario usuario) {
    usuario.setDeleted(false);
    usuario.setDeletedAt(null);
    // Hash password before saving
    if (usuario.getPassword() != null) {
      usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }
    return usuarioRepository.save(usuario);
  }

  @Override
  public Usuario obtenerUsuarioPorId(Long id) {
    return usuarioRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id));
  }

  @Override
  public Usuario eliminarLogicamente(Long id) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id));
    u.setDeleted(true);
    u.setDeletedAt(LocalDateTime.now());
    return usuarioRepository.save(u);
  }

  @Override
  public void eliminarFisicamente(Long id) {
    if (!usuarioRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id);
    }
    usuarioRepository.deleteById(id);
  }
}
