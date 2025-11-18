package com.hackerank.projectmanager.dummy.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.*;

@JsonDeserialize(builder = ProductoRequest.Builder.class)
public class ProductoRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private final String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private final String descripcion;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 2 decimales")
    private final Double precio;

    @NotBlank(message = "La categoría es requerida")
    private final String categoria;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;

    @JsonProperty("disponible")
    private final Boolean disponible;

    private ProductoRequest(Builder builder) {
        this.nombre = builder.nombre;
        this.descripcion = builder.descripcion;
        this.precio = builder.precio;
        this.categoria = builder.categoria;
        this.stock = builder.stock;
        this.disponible = builder.disponible;
    }

    // Getters
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

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String nombre;
        private String descripcion;
        private Double precio;
        private String categoria;
        private Integer stock;
        private Boolean disponible;

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

        public ProductoRequest build() {
            return new ProductoRequest(this);
        }
    }
}
