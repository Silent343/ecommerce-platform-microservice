package com.ecommerce.notification.application.port.in;

import com.ecommerce.notification.application.dto.response.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface QueryNotificationUseCase {
    List<NotificationResponse> getForRecipient(UUID recipientId);
    NotificationResponse markAsRead(UUID notificationId);
}
