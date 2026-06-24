package com.ecommerce.notification.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Destinatario de la notificación: un usuario que vive en users-service. */
public final class RecipientId {

    private final UUID value;

    private RecipientId(UUID value) {
        this.value = value;
    }

    public static RecipientId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("RecipientId no puede ser null");
        }
        return new RecipientId(value);
    }

    public static RecipientId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipientId that)) return false;
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
