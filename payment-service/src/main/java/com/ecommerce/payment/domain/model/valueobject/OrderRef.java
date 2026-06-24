package com.ecommerce.payment.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Referencia al pedido (vive en orders-service). Solo se guarda el UUID:
 * sin FK entre servicios.
 */
public final class OrderRef {

    private final UUID value;

    private OrderRef(UUID value) {
        this.value = value;
    }

    public static OrderRef of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderRef no puede ser null");
        }
        return new OrderRef(value);
    }

    public static OrderRef of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderRef that)) return false;
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
