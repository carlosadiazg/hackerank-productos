package com.hackerank.projectmanager.domain.repository;

import com.hackerank.projectmanager.domain.Producto;

import java.util.List;
import java.util.Optional;

// Interface Segregation Principle - Interfaces específicas
public interface ProductoRepository {
    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByDisponible(Boolean disponible);

    List<Producto> findByNombreContaining(String nombre);

    Producto save(Producto producto);

    void deleteById(Long id);

    boolean existsById(Long id);

    long count();
}
