package com.hackerank.projectmanager.presentation.controller;

import com.hackerank.projectmanager.application.service.ProductoMapper;
import com.hackerank.projectmanager.domain.Producto;
import com.hackerank.projectmanager.domain.service.ProductoService;
import com.hackerank.projectmanager.presentation.dto.ProductoRequest;
import com.hackerank.projectmanager.presentation.dto.ProductoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projectmanager")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponse>> obtenerTodosProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Producto> productos = productoService.obtenerTodosProductos();

        // Implementación simple de paginación en memoria
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productos.size());

        List<ProductoResponse> content = productos.stream()
                .skip(start)
                .limit(end - start)
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        Page<ProductoResponse> pageResponse = new PageImpl<>(
                content, pageable, productos.size());

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(producto -> ResponseEntity.ok(productoMapper.toResponse(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosPorCategoria(
            @PathVariable String categoria) {

        List<ProductoResponse> response = productoService.buscarProductosPorCategoria(categoria)
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscarProductos(
            @RequestParam String nombre) {

        List<ProductoResponse> response = productoService.buscarProductosPorNombre(nombre)
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(
            @Valid @RequestBody ProductoRequest request) {

        Producto producto = productoMapper.toDomain(request);
        Producto productoGuardado = productoService.crearProducto(producto);
        ProductoResponse response = productoMapper.toResponse(productoGuardado);

        return ResponseEntity.created(URI.create("/api/v1/productos/" + productoGuardado.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        return productoService.obtenerProductoPorId(id)
                .map(existing -> {
                    Producto updated = productoMapper.updateDomainFromRequest(existing, request);
                    Producto productoActualizado = productoService.actualizarProducto(id, updated)
                            .orElseThrow(() -> new RuntimeException("Error actualizando producto"));
                    return ResponseEntity.ok(productoMapper.toResponse(productoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosDisponibles() {
        List<ProductoResponse> response = productoService.buscarProductosDisponibles()
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
