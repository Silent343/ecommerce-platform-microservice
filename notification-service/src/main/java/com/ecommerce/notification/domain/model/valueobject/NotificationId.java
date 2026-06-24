package com.ecommerce.notification.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Identificador único de una notificación. */
public final class NotificationId {

    private final UUID value;

    private NotificationId(UUID value) {
        this.value = value;
    }

    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }

    public static NotificationId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("NotificationId no puede ser null");
        }
        return new NotificationId(value);
    }

    public static NotificationId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationId that)) return false;
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
