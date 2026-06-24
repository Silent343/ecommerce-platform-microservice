package com.ecommerce.products.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Identificador único de un producto, basado en UUID. */
public final class ProductId {

    private final UUID value;

    private ProductId(UUID value) {
        this.value = value;
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductId no puede ser null");
        }
        return new ProductId(value);
    }

    public static ProductId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductId that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
