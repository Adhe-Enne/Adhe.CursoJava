package com.techlab.services.impl;

import com.techlab.models.categorias.Categoria;
import com.techlab.repositories.CategoriaRepository;
import com.techlab.repositories.ProductoRepository;
import com.techlab.services.ICategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

  private final CategoriaRepository categoriaRepository;
  private final ProductoRepository productoRepository;

  public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
    this.categoriaRepository = categoriaRepository;
    this.productoRepository = productoRepository;
  }

  @Override
  public List<Categoria> listarCategorias() {
    return categoriaRepository.findAll();
  }

  @Override
  @Transactional
  public Categoria crearCategoria(Categoria categoria) {
    return categoriaRepository.save(categoria);
  }

  @Override
  public Categoria obtenerCategoriaPorId(Long id) {
    return categoriaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + id));
  }

  @Override
  @Transactional
  public Categoria actualizarCategoria(Long id, Categoria categoria) {
    Categoria existente = obtenerCategoriaPorId(id);
    existente.setNombre(categoria.getNombre());
    existente.setDescripcion(categoria.getDescripcion());
    existente.setActivo(categoria.isActivo());
    return categoriaRepository.save(existente);
  }

  @Override
  @Transactional
  public void eliminarCategoria(Long id) {
    // Prevenir eliminación si hay productos asociados
    boolean tieneProductos = productoRepository.existsByCategoriaId(id);
    if (tieneProductos) {
      throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados");
    }
    categoriaRepository.deleteById(id);
  }
}
