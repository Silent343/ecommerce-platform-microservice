package com.ecommerce.notification.domain.repository;

import com.ecommerce.notification.domain.model.aggregate.Notification;
import com.ecommerce.notification.domain.model.valueobject.NotificationId;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(NotificationId id);
    List<Notification> findByRecipient(RecipientId recipientId);
}
