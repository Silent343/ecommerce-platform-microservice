package com.ecommerce.payment.domain.model.entity;

import com.ecommerce.payment.domain.model.valueobject.Money;

import java.time.Instant;
import java.util.UUID;

/**
 * Reembolso asociado a un pago. Vive dentro del agregado Payment.
 */
public class Refund {

    private final UUID refundId;
    private final Money amount;
    private final String reason;
    private final Instant refundedAt;

    public Refund(UUID refundId, Money amount, String reason, Instant refundedAt) {
        this.refundId = refundId != null ? refundId : UUID.randomUUID();
        this.amount = amount;
        this.reason = reason;
        this.refundedAt = refundedAt != null ? refundedAt : Instant.now();
    }

    public static Refund create(Money amount, String reason) {
        return new Refund(UUID.randomUUID(), amount, reason, Instant.now());
    }

    public UUID getRefundId() {
        return refundId;
    }

    public Money getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public Instant getRefundedAt() {
        return refundedAt;
    }
}
