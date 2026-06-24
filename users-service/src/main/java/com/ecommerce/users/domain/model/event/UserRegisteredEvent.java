package com.ecommerce.users.domain.model.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de dominio que se emite cuando un usuario se registra correctamente.
 * Se publicará a RabbitMQ para que otros servicios reaccionen
 * (ej: notification-service envía un correo de bienvenida).
 */
public record UserRegisteredEvent(
        UUID userId,
        String email,
        String fullName,
        Instant occurredOn
) {
}
