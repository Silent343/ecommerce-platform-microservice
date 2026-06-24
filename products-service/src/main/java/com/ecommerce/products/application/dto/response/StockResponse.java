package com.ecommerce.products.application.dto.response;

import java.util.UUID;

public record StockResponse(
        UUID productId,
        String sku,
        int quantity,
        boolean outOfStock
) {
}
