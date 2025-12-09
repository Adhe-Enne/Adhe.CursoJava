package com.techlab.services;

import com.techlab.models.categorias.Categoria;

import java.util.List;

public interface ICategoriaService {
  List<Categoria> listarCategorias();

  Categoria crearCategoria(Categoria categoria);

  Categoria obtenerCategoriaPorId(Long id);

  Categoria actualizarCategoria(Long id, Categoria categoria);

  void eliminarCategoria(Long id);
}
