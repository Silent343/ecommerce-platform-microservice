package com.ecommerce.notification.infrastructure.persistence.mapper;

import com.ecommerce.notification.domain.model.aggregate.Notification;
import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;
import com.ecommerce.notification.domain.model.valueobject.NotificationId;
import com.ecommerce.notification.domain.model.valueobject.NotificationType;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;
import com.ecommerce.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationJpaEntity toJpa(Notification n) {
        return new NotificationJpaEntity(
                n.getId().value(), n.getRecipientId().value(), n.getType().name(),
                n.getChannel().name(), n.getTitle(), n.getMessage(), n.isRead(), n.getCreatedAt());
    }

    public Notification toDomain(NotificationJpaEntity e) {
        return Notification.reconstitute(
                NotificationId.of(e.getId()),
                RecipientId.of(e.getRecipientId()),
                NotificationType.valueOf(e.getType()),
                NotificationChannel.valueOf(e.getChannel()),
                e.getTitle(), e.getMessage(), e.isRead(), e.getCreatedAt());
    }
}
