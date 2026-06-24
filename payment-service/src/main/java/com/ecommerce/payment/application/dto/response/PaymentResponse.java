package com.ecommerce.payment.application.dto.response;

import com.ecommerce.payment.domain.model.aggregate.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        BigDecimal amount,
        String currency,
        String method,
        String status,
        String receiptNumber,
        Instant createdAt
) {
    public static PaymentResponse from(Payment payment) {
        String receiptNumber = payment.getReceipt() != null
                ? payment.getReceipt().receiptNumber() : null;
        return new PaymentResponse(
                payment.getId().value(),
                payment.getOrderRef().value(),
                payment.getCustomerId().value(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                receiptNumber,
                payment.getCreatedAt());
    }
}
