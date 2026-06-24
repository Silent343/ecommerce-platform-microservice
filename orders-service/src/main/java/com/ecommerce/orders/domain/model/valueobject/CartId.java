package com.ecommerce.orders.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Identificador único basado en UUID. */
public final class CartId {

    private final UUID value;

    private CartId(UUID value) {
        this.value = value;
    }

    public static CartId generate() {
        return new CartId(UUID.randomUUID());
    }

    public static CartId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CartId no puede ser null");
        }
        return new CartId(value);
    }

    public static CartId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartId that)) return false;
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
