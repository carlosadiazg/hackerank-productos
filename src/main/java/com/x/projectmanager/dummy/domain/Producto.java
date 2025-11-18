package com.x.projectmanager.dummy.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.x.projectmanager.dummy.domain.model.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonDeserialize(builder = Producto.Builder.class)
public class Producto extends BaseEntity {
    private final String nombre;
    private final String descripcion;
    private final Double precio;
    private final String categoria;
    private final Integer stock;
    private final Boolean disponible;

    // Constructor privado para usar Builder
    private Producto(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.descripcion = builder.descripcion;
        this.precio = builder.precio;
        this.categoria = builder.categoria;
        this.stock = builder.stock;
        this.disponible = builder.disponible;
        this.fechaCreacion = builder.fechaCreacion;
        this.fechaActualizacion = builder.fechaActualizacion;
    }

    // Getters (inmutabilidad)
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getStock() {
        return stock;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    // Métodos de negocio
    public boolean tieneStock() {
        return disponible && stock != null && stock > 0;
    }

    public boolean esDeCategoria(String categoria) {
        return this.categoria.equalsIgnoreCase(categoria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Builder Pattern
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precio;
        private String categoria;
        private Integer stock;
        private Boolean disponible;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaActualizacion;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder precio(Double precio) {
            this.precio = precio;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder disponible(Boolean disponible) {
            this.disponible = disponible;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder fechaActualizacion(LocalDateTime fechaActualizacion) {
            this.fechaActualizacion = fechaActualizacion;
            return this;
        }

        public Producto build() {
            // Validaciones de negocio
            Objects.requireNonNull(nombre, "El nombre es requerido");
            Objects.requireNonNull(precio, "El precio es requerido");
            if (precio < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            if (stock != null && stock < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }

            return new Producto(this);
        }
    }
}