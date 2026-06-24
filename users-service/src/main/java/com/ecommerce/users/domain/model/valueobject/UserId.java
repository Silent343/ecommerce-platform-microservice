package com.ecommerce.users.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador único de un usuario, basado en UUID.
 * Usar UUID evita exponer IDs secuenciales y facilita la generación
 * distribuida entre microservicios sin coordinación central.
 */
public final class UserId {

    private final UUID value;

    private UserId(UUID value) {
        this.value = value;
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("UserId no puede ser null");
        }
        return new UserId(value);
    }

    public static UserId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId userId)) return false;
        return value.equals(userId.value);
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
