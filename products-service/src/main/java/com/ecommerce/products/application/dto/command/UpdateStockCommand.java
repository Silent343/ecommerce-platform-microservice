package com.ecommerce.products.application.dto.command;

import java.util.UUID;

/**
 * Command para ajustar stock. 'operation' indica el tipo de ajuste:
 * SET (fija el valor), INCREASE (suma), DECREASE (resta).
 */
public record UpdateStockCommand(
        UUID productId,
        String operation,
        int quantity
) {
}
