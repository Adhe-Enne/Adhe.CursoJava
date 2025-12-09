package com.techlab.services.impl;

import com.techlab.repositories.ProductoRepository;
import com.techlab.services.IProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techlab.excepciones.ResourceNotFoundException;
import com.techlab.models.productos.Producto;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements IProductoService {

  private final ProductoRepository productoRepository;

  public ProductoServiceImpl(ProductoRepository productoRepository) {
    this.productoRepository = productoRepository;
  }

  @Override
  public List<Producto> listarProductos() {
    return productoRepository.findAll();
  }

  @Override
  public Producto obtenerProductoPorId(Long id) {
    return productoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
  }

  @Override
  public Producto crearProducto(Producto producto) {
    producto.setId(null);
    return productoRepository.save(producto);
  }

  @Override
  public Producto actualizarProducto(Long id, Producto producto) {
    Producto existente = productoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    existente.setNombre(producto.getNombre());
    existente.setDescripcion(producto.getDescripcion());
    existente.setPrecio(producto.getPrecio());
    existente.setStock(producto.getStock());
    existente.setCategoriaId(producto.getCategoriaId());
    existente.setImagenUrl(producto.getImagenUrl());
    return productoRepository.save(existente);
  }

  @Override
  public void eliminarProducto(Long id) {
    if (!productoRepository.existsById(id)) {
      throw new ResourceNotFoundException("Producto no encontrado");
    }
    productoRepository.deleteById(id);
  }
}
