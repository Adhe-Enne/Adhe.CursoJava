package com.techlab.services.impl;

import com.techlab.models.pedidos.LineaPedido;
import com.techlab.models.pedidos.Pedido;
import com.techlab.models.productos.Producto;
import com.techlab.repositories.PedidoRepository;
import com.techlab.repositories.ProductoRepository;
import com.techlab.services.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techlab.excepciones.BadRequestException;
import com.techlab.excepciones.ResourceNotFoundException;
import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements IPedidoService {

  private final PedidoRepository pedidoRepository;
  private final ProductoRepository productoRepository;

  @Autowired
  public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
    this.pedidoRepository = pedidoRepository;
    this.productoRepository = productoRepository;
  }

  @Override
  @Transactional
  public Pedido crearPedido(Pedido pedido) {
    validatePedidoBasico(pedido);
    double total = 0.0;
    for (LineaPedido linea : pedido.getLineasPedido()) {
      total += procesarLineaPedido(pedido, linea);
    }
    pedido.setTotal(total);
    // Asegurar estado por defecto
    if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
      pedido.setEstado("PENDIENTE");
    }
    return pedidoRepository.save(pedido);
  }

  @Override
  public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
    return pedidoRepository.findByUsuarioId(usuarioId);
  }

  @Override
  public List<Pedido> listarPedidos() {
    return pedidoRepository.findAll();
  }

  @Override
  public Pedido actualizarEstadoPedido(Long id, String estado) {
    if (estado == null || estado.trim().isEmpty()) {
      throw new BadRequestException("Estado inválido");
    }
    Pedido p = pedidoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    // Aquí podríamos validar transiciones de estado si es necesario
    p.setEstado(estado.trim().toUpperCase());
    return pedidoRepository.save(p);
  }

  @Override
  public void eliminarPedido(Long id) {
    if (!pedidoRepository.existsById(id)) {
      throw new ResourceNotFoundException("Pedido no encontrado: " + id);
    }
    pedidoRepository.deleteById(id);
  }

  private void validatePedidoBasico(Pedido pedido) {
    if (pedido == null) {
      throw new BadRequestException("Pedido no puede ser nulo");
    }
    if (pedido.getUsuarioId() == null) {
      throw new BadRequestException("usuarioId es requerido");
    }
    if (pedido.getFechaCreacion() == null) {
      pedido.setFechaCreacion(LocalDateTime.now());
    }
    if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
      throw new BadRequestException("El pedido debe contener al menos una línea");
    }
  }

  private double procesarLineaPedido(Pedido pedido, LineaPedido linea) {
    if (linea.getProductoId() == null) {
      throw new BadRequestException("productoId es requerido en linea de pedido");
    }
    Producto producto = productoRepository.findById(linea.getProductoId())
        .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + linea.getProductoId()));

    int cantidad = (linea.getCantidad() != null ? linea.getCantidad() : 0);
    if (cantidad <= 0) {
      throw new BadRequestException("Cantidad inválida para el producto: " + linea.getProductoId());
    }

    if (producto.getStock() == null || producto.getStock() < cantidad) {
      throw new StockInsuficienteException("Stock insuficiente para producto: " + linea.getProductoId());
    }

    if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
      throw new BadRequestException("Precio inválido para producto: " + linea.getProductoId());
    }

    double subtotal = producto.getPrecio() * cantidad;
    linea.setSubtotal(subtotal);
    linea.setPedido(pedido);

    // Descontar stock y persistir el cambio
    producto.setStock(producto.getStock() - cantidad);
    productoRepository.save(producto);

    return subtotal;
  }
}
