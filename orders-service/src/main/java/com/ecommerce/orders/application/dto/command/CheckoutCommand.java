package com.ecommerce.orders.application.dto.command;

import java.util.UUID;

public record CheckoutCommand(
        UUID customerId
) {
}
