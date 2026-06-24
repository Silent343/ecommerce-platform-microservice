package com.ecommerce.notification.application.dto.response;

import com.ecommerce.notification.domain.model.aggregate.Notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID recipientId,
        String type,
        String channel,
        String title,
        String message,
        boolean read,
        Instant createdAt
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId().value(),
                n.getRecipientId().value(),
                n.getType().name(),
                n.getChannel().name(),
                n.getTitle(),
                n.getMessage(),
                n.isRead(),
                n.getCreatedAt());
    }
}
