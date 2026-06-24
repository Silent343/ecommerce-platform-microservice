package com.ecommerce.products.domain.model.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Evento de dominio emitido cuando se crea un producto nuevo en el catálogo. */
public record ProductCreatedEvent(
        UUID productId,
        String sku,
        String name,
        BigDecimal price,
        UUID sellerId,
        Instant occurredOn
) {
}
