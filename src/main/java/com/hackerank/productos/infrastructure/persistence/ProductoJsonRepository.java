package com.hackerank.productos.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerank.productos.domain.Producto;
import com.hackerank.productos.domain.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class ProductoJsonRepository implements ProductoRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProductoJsonRepository.class);
    private final Map<Long, Producto> productosMap = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1000);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("data/productos.json")
    private String jsonFilePath;

    @PostConstruct
    public void init() {
        cargarProductosDesdeJSON();
    }

    private void cargarProductosDesdeJSON() {
        try {
            File file = new ClassPathResource("data/productos.json").getFile();
            //File file = ResourceUtils.getFile(jsonFilePath);
            JsonDataWrapper wrapper = objectMapper.readValue(file, JsonDataWrapper.class);

            if (wrapper.getProductos() != null) {
                wrapper.getProductos().forEach(producto -> {
                    productosMap.put(producto.getId(), producto);
                    sequence.updateAndGet(current -> Math.max(current, producto.getId() + 1));
                });
                logger.info("Cargados {} productos desde JSON", productosMap.size());
            }
        } catch (IOException e) {
            logger.error("Error al cargar el archivo JSON de productos: {}", e.getMessage());
            throw new RuntimeException("Error inicializando repositorio de productos", e);
        }
    }

    @Override
    public List<Producto> findAll() {
        return new ArrayList<>(productosMap.values());
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return Optional.ofNullable(productosMap.get(id));
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        return productosMap.values().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByDisponible(Boolean disponible) {
        return productosMap.values().stream()
                .filter(p -> p.getDisponible().equals(disponible))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByNombreContaining(String nombre) {
        return productosMap.values().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(sequence.getAndIncrement());
        }
        productosMap.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public void deleteById(Long id) {
        productosMap.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productosMap.containsKey(id);
    }

    @Override
    public long count() {
        return productosMap.size();
    }

    // Clase interna para el wrapper del JSON
    private static class JsonDataWrapper {
        private List<Producto> productos;

        public List<Producto> getProductos() {
            return productos;
        }

        public void setProductos(List<Producto> productos) {
            this.productos = productos;
        }
    }
}
