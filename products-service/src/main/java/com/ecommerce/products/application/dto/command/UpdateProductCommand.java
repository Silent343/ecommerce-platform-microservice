package com.ecommerce.products.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        String name,
        String description,
        BigDecimal price,
        String currency,
        UUID categoryId
) {
}
