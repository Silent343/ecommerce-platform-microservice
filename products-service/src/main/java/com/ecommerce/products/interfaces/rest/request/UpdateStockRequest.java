package com.ecommerce.products.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStockRequest(
        @NotBlank String operation,
        @PositiveOrZero int quantity
) {
}
