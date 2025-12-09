package com.techlab.services.impl;

import com.techlab.models.pedidos.LineaPedido;
import com.techlab.models.pedidos.Pedido;
import com.techlab.models.productos.Producto;
import com.techlab.repositories.PedidoRepository;
import com.techlab.repositories.ProductoRepository;
import com.techlab.services.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
    if (pedido.getFechaCreacion() == null) {
      pedido.setFechaCreacion(LocalDateTime.now());
    }

    double total = 0.0;
    if (pedido.getLineasPedido() != null) {
      for (LineaPedido linea : pedido.getLineasPedido()) {
        Producto producto = productoRepository.findById(linea.getProductoId())
            .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + linea.getProductoId()));

        int cantidad = (linea.getCantidad() != null ? linea.getCantidad() : 0);
        if (cantidad < 0) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Cantidad inválida para el producto: " + linea.getProductoId());
        }

        if (producto.getStock() == null || producto.getStock() < cantidad) {
          throw new StockInsuficienteException("Stock insuficiente para producto: " + linea.getProductoId());
        }

        double subtotal = producto.getPrecio() * cantidad;
        linea.setSubtotal(subtotal);
        linea.setPedido(pedido);
        total += subtotal;

        // Descontar stock y persistir el cambio
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
      }
    }

    pedido.setTotal(total);
    return pedidoRepository.save(pedido);
  }

  @Override
  public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
    return pedidoRepository.findByUsuarioId(usuarioId);
  }

  public List<Pedido> listarPedidos() {
    return pedidoRepository.findAll();
  }

  @Override
  public Pedido actualizarEstadoPedido(Long id, String estado) {
    Pedido p = pedidoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado: " + id));
    p.setEstado(estado);
    return pedidoRepository.save(p);
  }
}
