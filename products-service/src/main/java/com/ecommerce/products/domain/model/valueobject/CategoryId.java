package com.ecommerce.products.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Identificador único de una categoría. */
public final class CategoryId {

    private final UUID value;

    private CategoryId(UUID value) {
        this.value = value;
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CategoryId no puede ser null");
        }
        return new CategoryId(value);
    }

    public static CategoryId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryId that)) return false;
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
