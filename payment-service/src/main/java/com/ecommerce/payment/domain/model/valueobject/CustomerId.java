package com.ecommerce.payment.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Referencia al cliente (vive en users-service). */
public final class CustomerId {

    private final UUID value;

    private CustomerId(UUID value) {
        this.value = value;
    }

    public static CustomerId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CustomerId no puede ser null");
        }
        return new CustomerId(value);
    }

    public static CustomerId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerId that)) return false;
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
