package com.ecommerce.products.domain.model.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de dominio emitido cuando cambia el stock de un producto.
 * Lo consumen otros servicios (ej: notification-service avisa de bajo stock,
 * orders-service confirma reservas).
 */
public record StockUpdatedEvent(
        UUID productId,
        String sku,
        int previousQuantity,
        int newQuantity,
        Instant occurredOn
) {
}
