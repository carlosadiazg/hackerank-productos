package com.hackerank.productos.presentation.controller;

import com.hackerank.productos.application.service.ProductoMapper;
import com.hackerank.productos.domain.Producto;
import com.hackerank.productos.domain.service.ProductoService;
import com.hackerank.productos.presentation.dto.ProductoRequest;
import com.hackerank.productos.presentation.dto.ProductoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;
    private ProductoResponse productoResponse;
    private ProductoRequest productoRequest;

    @BeforeEach
    void setUp() {

        producto = new Producto.Builder()
                .id(1L)
                .nombre("Test Product")
                .categoria("Electronics")
                .precio(100.0)
                .disponible(true)
                .build();

        productoResponse = new ProductoResponse.Builder()
                .id(1L)
                .nombre("Test Product")
                .categoria("Electronics")
                .precio(100.0)
                .disponible(true)
                .build();

        productoRequest = new ProductoRequest.Builder()
                .nombre("Test Product")
                .categoria("Electronics")
                .precio(100.0)
                .disponible(true)
                .build();
    }

    @Test
    void obtenerTodosProductos_ShouldReturnPaginatedProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto,
                new Producto.Builder().id(2L).nombre("Product 2").precio(1500.0).build());
        List<ProductoResponse> productoResponses = Arrays.asList(productoResponse,
                new ProductoResponse.Builder().id(2L).nombre("Product 2").build());

        when(productoService.obtenerTodosProductos()).thenReturn(productos);
        when(productoMapper.toResponse(any(Producto.class)))
                .thenReturn(productoResponses.get(0))
                .thenReturn(productoResponses.get(1));

        // Act
        ResponseEntity<Page<ProductoResponse>> response =
                productoController.obtenerTodosProductos(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Page<ProductoResponse> page = response.getBody();
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(2, page.getContent().size());

        verify(productoService).obtenerTodosProductos();
        verify(productoMapper, times(2)).toResponse(any(Producto.class));
    }

    @Test
    void obtenerTodosProductos_WithEmptyList_ShouldReturnEmptyPage() {
        // Arrange
        when(productoService.obtenerTodosProductos()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Page<ProductoResponse>> response =
                productoController.obtenerTodosProductos(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().getTotalElements());

        verify(productoService).obtenerTodosProductos();
        verify(productoMapper, never()).toResponse(any(Producto.class));
    }

    @Test
    void obtenerProductoPorId_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

        // Act
        ResponseEntity<ProductoResponse> response = productoController.obtenerProductoPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Product", response.getBody().getNombre());

        verify(productoService).obtenerProductoPorId(1L);
        verify(productoMapper).toResponse(producto);
    }

    @Test
    void obtenerProductoPorId_WhenProductNotExists_ShouldReturnNotFound() {
        // Arrange
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProductoResponse> response = productoController.obtenerProductoPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(productoService).obtenerProductoPorId(1L);
        verify(productoMapper, never()).toResponse(any(Producto.class));
    }

    @Test
    void obtenerProductosPorCategoria_ShouldReturnProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        List<ProductoResponse> productoResponses = Arrays.asList(productoResponse);

        when(productoService.buscarProductosPorCategoria("Electronics")).thenReturn(productos);
        when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

        // Act
        ResponseEntity<List<ProductoResponse>> response =
                productoController.obtenerProductosPorCategoria("Electronics");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Electronics", response.getBody().get(0).getCategoria());

        verify(productoService).buscarProductosPorCategoria("Electronics");
        verify(productoMapper).toResponse(producto);
    }

    @Test
    void obtenerProductosPorCategoria_WhenNoProducts_ShouldReturnEmptyList() {
        // Arrange
        when(productoService.buscarProductosPorCategoria("Unknown")).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ProductoResponse>> response =
                productoController.obtenerProductosPorCategoria("Unknown");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(productoService).buscarProductosPorCategoria("Unknown");
        verify(productoMapper, never()).toResponse(any(Producto.class));
    }

    @Test
    void buscarProductos_ShouldReturnMatchingProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        List<ProductoResponse> productoResponses = Arrays.asList(productoResponse);

        when(productoService.buscarProductosPorNombre("Test")).thenReturn(productos);
        when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

        // Act
        ResponseEntity<List<ProductoResponse>> response =
                productoController.buscarProductos("Test");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Product", response.getBody().get(0).getNombre());

        verify(productoService).buscarProductosPorNombre("Test");
        verify(productoMapper).toResponse(producto);
    }

    @Test
    void crearProducto_ShouldCreateAndReturnProduct() {
        // Arrange
        when(productoMapper.toDomain(productoRequest)).thenReturn(producto);
        when(productoService.crearProducto(producto)).thenReturn(producto);
        when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

        // Act
        ResponseEntity<ProductoResponse> response = productoController.crearProducto(productoRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/v1/productos/1"));

        verify(productoMapper).toDomain(productoRequest);
        verify(productoService).crearProducto(producto);
        verify(productoMapper).toResponse(producto);
    }

    @Test
    void actualizarProducto_WhenProductExists_ShouldUpdateAndReturnProduct() {
        // Arrange
        Producto updatedProducto = new Producto.Builder()
                .id(1L)
                .nombre("Updated Product")
                .categoria("Electronics")
                .precio(150.0)
                .disponible(true)
                .build();

        ProductoResponse updatedResponse = new ProductoResponse.Builder()
                .id(1L)
                .nombre("Updated Product")
                .categoria("Electronics")
                .precio(150.0)
                .disponible(true)
                .build();

        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.updateDomainFromRequest(producto, productoRequest)).thenReturn(updatedProducto);
        when(productoService.actualizarProducto(1L, updatedProducto)).thenReturn(Optional.of(updatedProducto));
        when(productoMapper.toResponse(updatedProducto)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<ProductoResponse> response =
                productoController.actualizarProducto(1L, productoRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Product", response.getBody().getNombre());
        assertEquals(150.0, response.getBody().getPrecio());

        verify(productoService).obtenerProductoPorId(1L);
        verify(productoMapper).updateDomainFromRequest(producto, productoRequest);
        verify(productoService).actualizarProducto(1L, updatedProducto);
        verify(productoMapper).toResponse(updatedProducto);
    }

    @Test
    void actualizarProducto_WhenProductNotExists_ShouldReturnNotFound() {
        // Arrange
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProductoResponse> response =
                productoController.actualizarProducto(1L, productoRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(productoService).obtenerProductoPorId(1L);
        verify(productoMapper, never()).updateDomainFromRequest(any(), any());
        verify(productoService, never()).actualizarProducto(anyLong(), any());
        verify(productoMapper, never()).toResponse(any());
    }

    @Test
    void eliminarProducto_WhenProductExists_ShouldReturnNoContent() {
        // Arrange
        when(productoService.eliminarProducto(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(productoService).eliminarProducto(1L);
    }

    @Test
    void eliminarProducto_WhenProductNotExists_ShouldReturnNotFound() {
        // Arrange
        when(productoService.eliminarProducto(1L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(productoService).eliminarProducto(1L);
    }

    @Test
    void obtenerProductosDisponibles_ShouldReturnAvailableProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto);
        List<ProductoResponse> productoResponses = Arrays.asList(productoResponse);

        when(productoService.buscarProductosDisponibles()).thenReturn(productos);
        when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

        // Act
        ResponseEntity<List<ProductoResponse>> response =
                productoController.obtenerProductosDisponibles();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        //assertTrue(response.getBody().get(0).isDisponible());

        verify(productoService).buscarProductosDisponibles();
        verify(productoMapper).toResponse(producto);
    }

    @Test
    void obtenerTodosProductos_WithPagination_ShouldHandleDifferentPages() {
        // Arrange
        List<Producto> productos = Arrays.asList(
                producto,
                new Producto.Builder().id(2L).nombre("Product 2").precio(1000.0).build(),
                new Producto.Builder().id(3L).nombre("Product 3").precio(500.0).build()
        );

        List<ProductoResponse> productoResponses = Arrays.asList(
                productoResponse,
                new ProductoResponse.Builder().id(2L).nombre("Product 2").build(),
                new ProductoResponse.Builder().id(3L).nombre("Product 3").build()
        );

        when(productoService.obtenerTodosProductos()).thenReturn(productos);
        when(productoMapper.toResponse(any(Producto.class)))
                .thenReturn(productoResponses.get(0))
                .thenReturn(productoResponses.get(1))
                .thenReturn(productoResponses.get(2));

        // Act - Test first page with 2 items
        ResponseEntity<Page<ProductoResponse>> response =
                productoController.obtenerTodosProductos(0, 2);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Page<ProductoResponse> page = response.getBody();
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getTotalPages());

        verify(productoService).obtenerTodosProductos();
        verify(productoMapper, times(2)).toResponse(any(Producto.class));
    }
}