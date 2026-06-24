package com.ecommerce.notification.domain.repository;

import com.ecommerce.notification.domain.model.aggregate.NotificationPreference;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;

import java.util.Optional;

public interface PreferenceRepository {
    NotificationPreference save(NotificationPreference preference);
    Optional<NotificationPreference> findByRecipient(RecipientId recipientId);
}
