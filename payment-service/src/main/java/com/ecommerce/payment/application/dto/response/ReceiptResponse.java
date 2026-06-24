package com.ecommerce.payment.application.dto.response;

import com.ecommerce.payment.domain.model.aggregate.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReceiptResponse(
        String receiptNumber,
        UUID paymentId,
        UUID orderId,
        BigDecimal amount,
        String currency,
        Instant issuedAt
) {
    public static ReceiptResponse from(Payment payment) {
        if (payment.getReceipt() == null) {
            return null;
        }
        return new ReceiptResponse(
                payment.getReceipt().receiptNumber(),
                payment.getId().value(),
                payment.getOrderRef().value(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getReceipt().issuedAt());
    }
}
