package com.techlab.services;

import java.util.List;

import com.techlab.models.usuarios.Usuario;

public interface IUsuarioService {
  Usuario crearUsuario(Usuario usuario);

  List<Usuario> listarUsuarios();

  Usuario obtenerUsuarioPorId(Long id);

  Usuario eliminarLogicamente(Long id);

  void eliminarFisicamente(Long id);
}
