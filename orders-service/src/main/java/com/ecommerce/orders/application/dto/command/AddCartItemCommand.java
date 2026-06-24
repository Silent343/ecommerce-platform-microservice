package com.ecommerce.orders.application.dto.command;

import java.util.UUID;

public record AddCartItemCommand(
        UUID customerId,
        UUID productId,
        int quantity
) {
}
