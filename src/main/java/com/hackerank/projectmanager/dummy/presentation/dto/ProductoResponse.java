package com.hackerank.projectmanager.dummy.presentation.dto;

// DTO para responses

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoResponse {
    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final Double precio;
    private final String categoria;
    private final Integer stock;
    private final Boolean disponible;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaActualizacion;

    private ProductoResponse(Builder builder) {
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

    // Getters
    public Long getId() {
        return id;
    }

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    // Builder
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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder fechaActualizacion(LocalDateTime fechaActualizacion) {
            this.fechaActualizacion = fechaActualizacion;
            return this;
        }

        public ProductoResponse build() {
            return new ProductoResponse(this);
        }
    }
}
