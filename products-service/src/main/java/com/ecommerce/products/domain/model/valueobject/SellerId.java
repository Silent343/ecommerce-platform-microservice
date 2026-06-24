package com.ecommerce.products.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Referencia al vendedor. El vendedor es un usuario (role SELLER) que vive en
 * users-service. Aquí solo guardamos su ID: NUNCA una FK entre servicios.
 * Cada microservicio es dueño de sus propios datos.
 */
public final class SellerId {

    private final UUID value;

    private SellerId(UUID value) {
        this.value = value;
    }

    public static SellerId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("SellerId no puede ser null");
        }
        return new SellerId(value);
    }

    public static SellerId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerId that)) return false;
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
