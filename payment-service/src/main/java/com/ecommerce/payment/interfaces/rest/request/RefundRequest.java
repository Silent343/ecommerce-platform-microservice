package com.ecommerce.payment.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record RefundRequest(
        @NotBlank String reason
) {
}
