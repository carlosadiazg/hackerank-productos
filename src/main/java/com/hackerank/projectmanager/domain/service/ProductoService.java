package com.hackerank.projectmanager.domain.service;


import com.hackerank.projectmanager.domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> obtenerTodosProductos();

    Optional<Producto> obtenerProductoPorId(Long id);

    List<Producto> buscarProductosPorCategoria(String categoria);

    List<Producto> buscarProductosDisponibles();

    List<Producto> buscarProductosPorNombre(String nombre);

    Producto crearProducto(Producto producto);

    Optional<Producto> actualizarProducto(Long id, Producto producto);

    boolean eliminarProducto(Long id);

    boolean existeProducto(Long id);
}
