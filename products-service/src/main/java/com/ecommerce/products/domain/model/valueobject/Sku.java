package com.ecommerce.products.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa el SKU (Stock Keeping Unit): código único de
 * inventario de un producto. Se normaliza a mayúsculas.
 */
public final class Sku {

    private final String value;

    private Sku(String value) {
        this.value = value;
    }

    public static Sku of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("El SKU no puede estar vacío");
        }
        String normalized = raw.trim().toUpperCase();
        if (normalized.length() < 3 || normalized.length() > 40) {
            throw new IllegalArgumentException("El SKU debe tener entre 3 y 40 caracteres");
        }
        return new Sku(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sku sku)) return false;
        return value.equals(sku.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
