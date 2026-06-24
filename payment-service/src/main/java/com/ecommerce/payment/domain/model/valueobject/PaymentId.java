package com.ecommerce.payment.domain.model.valueobject;

import java.util.Objects;
import java.util.UUID;

/** Identificador único de un pago. */
public final class PaymentId {

    private final UUID value;

    private PaymentId(UUID value) {
        this.value = value;
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }

    public static PaymentId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("PaymentId no puede ser null");
        }
        return new PaymentId(value);
    }

    public static PaymentId of(String value) {
        return of(UUID.fromString(value));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentId that)) return false;
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
