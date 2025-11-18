package com.hackerank.projectmanager.dummy.application.service;

import com.hackerank.projectmanager.dummy.domain.Producto;
import com.hackerank.projectmanager.dummy.presentation.dto.ProductoRequest;
import com.hackerank.projectmanager.dummy.presentation.dto.ProductoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductoMapper {

    public Producto toDomain(ProductoRequest request) {
        return new Producto.Builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .categoria(request.getCategoria())
                .stock(request.getStock())
                .disponible(request.getDisponible())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse.Builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .stock(producto.getStock())
                .disponible(producto.getDisponible())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }

    public Producto updateDomainFromRequest(Producto existing, ProductoRequest request) {
        return new Producto.Builder()
                .id(existing.getId())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .categoria(request.getCategoria())
                .stock(request.getStock())
                .disponible(request.getDisponible())
                .fechaCreacion(existing.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }
}