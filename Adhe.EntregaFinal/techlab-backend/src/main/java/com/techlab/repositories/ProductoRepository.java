package com.techlab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlab.models.productos.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
  boolean existsByCategoriaId(Long categoriaId);

  java.util.List<Producto> findByNombreContainingIgnoreCase(String nombre);

  java.util.List<Producto> findByCategoriaId(Long categoriaId);
}