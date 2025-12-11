package com.techlab.contracts;

import com.techlab.contracts.dtos.*;
import com.techlab.models.productos.Producto;
import com.techlab.models.categorias.Categoria;
import com.techlab.models.usuarios.Usuario;
import com.techlab.models.pedidos.LineaPedido;
import com.techlab.models.pedidos.Pedido;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

  // Producto
  public static ProductoDto toDto(Producto p) {
    if (p == null)
      return null;
    ProductoDto dto = new ProductoDto();
    dto.setId(p.getId());
    dto.setNombre(p.getNombre());
    dto.setDescripcion(p.getDescripcion());
    dto.setPrecio(p.getPrecio());
    dto.setStock(p.getStock());
    dto.setCategoriaId(p.getCategoriaId());
    dto.setImagenUrl(p.getImagenUrl());
    return dto;
  }

  public static Producto fromDto(ProductoDto dto) {
    if (dto == null)
      return null;
    Producto p = new Producto();
    p.setId(dto.getId());
    p.setNombre(dto.getNombre());
    p.setDescripcion(dto.getDescripcion());
    p.setPrecio(dto.getPrecio());
    p.setStock(dto.getStock());
    p.setCategoriaId(dto.getCategoriaId());
    p.setImagenUrl(dto.getImagenUrl());
    return p;
  }

  // Categoria
  public static CategoriaDto toDto(Categoria c) {
    if (c == null)
      return null;
    CategoriaDto dto = new CategoriaDto();
    dto.setId(c.getId());
    dto.setNombre(c.getNombre());
    dto.setDescripcion(c.getDescripcion());
    dto.setActivo(c.isActivo());
    return dto;
  }

  public static Categoria fromDto(CategoriaDto dto) {
    if (dto == null)
      return null;
    Categoria c = new Categoria();
    c.setId(dto.getId());
    c.setNombre(dto.getNombre());
    c.setDescripcion(dto.getDescripcion());
    c.setActivo(dto.isActivo());
    return c;
  }

  // Usuario
  public static UsuarioResponse toDto(Usuario u) {
    if (u == null)
      return null;
    UsuarioResponse r = new UsuarioResponse();
    r.setId(u.getId());
    r.setNombre(u.getNombre());
    r.setEmail(u.getEmail());
    r.setDeleted(u.getDeleted());
    r.setDeletedAt(u.getDeletedAt());
    return r;
  }

  public static Usuario fromRequest(UsuarioRequest req) {
    if (req == null)
      return null;
    Usuario u = new Usuario();
    u.setNombre(req.getNombre());
    u.setEmail(req.getEmail());
    u.setPassword(req.getPassword());
    return u;
  }

  // Pedido / Lineas
  public static LineaPedidoResponse toDto(LineaPedido lp) {
    if (lp == null)
      return null;
    LineaPedidoResponse r = new LineaPedidoResponse();
    r.setId(lp.getId());
    r.setProductoId(lp.getProductoId());
    r.setCantidad(lp.getCantidad());
    r.setSubtotal(lp.getSubtotal());
    return r;
  }

  public static LineaPedido fromRequest(LineaPedidoRequest req) {
    if (req == null)
      return null;
    LineaPedido lp = new LineaPedido();
    lp.setProductoId(req.getProductoId());
    lp.setCantidad(req.getCantidad());
    return lp;
  }

  public static PedidoResponse toDto(Pedido p) {
    if (p == null)
      return null;
    PedidoResponse r = new PedidoResponse();
    r.setId(p.getId());
    r.setUsuarioId(p.getUsuarioId());
    r.setEstado(p.getEstado());
    r.setFechaCreacion(p.getFechaCreacion());
    r.setTotal(p.getTotal());
    List<LineaPedidoResponse> lps = p.getLineasPedido() == null ? null
        : p.getLineasPedido().stream()
            .map(DtoMapper::toDto).collect(Collectors.toList());
    r.setLineasPedido(lps);
    return r;
  }

  public static Pedido fromRequest(PedidoRequest req) {
    if (req == null)
      return null;
    Pedido p = new Pedido();
    p.setUsuarioId(req.getUsuarioId());
    if (req.getItemsPedido() != null) {
      List<LineaPedido> lps = req.getItemsPedido().stream().map(DtoMapper::fromRequest).collect(Collectors.toList());
      lps.forEach(lp -> lp.setPedido(p));
      p.setLineasPedido(lps);
    }
    return p;
  }
}
