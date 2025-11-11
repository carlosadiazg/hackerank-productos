package com.hackerank.projectmanager.application.service;

import com.hackerank.projectmanager.domain.Producto;
import com.hackerank.projectmanager.domain.repository.ProductoRepository;
import com.hackerank.projectmanager.domain.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> buscarProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public List<Producto> buscarProductosDisponibles() {
        return productoRepository.findByDisponible(true);
    }

    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContaining(nombre);
    }

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Optional<Producto> actualizarProducto(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            return Optional.empty();
        }
        producto.setId(id);
        return Optional.of(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public boolean eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existeProducto(Long id) {
        return productoRepository.existsById(id);
    }
}
