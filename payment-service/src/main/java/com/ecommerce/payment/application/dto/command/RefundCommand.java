package com.ecommerce.payment.application.dto.command;

import java.util.UUID;

public record RefundCommand(
        UUID paymentId,
        String reason
) {
}
