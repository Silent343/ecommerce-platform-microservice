package com.ecommerce.notification.infrastructure.persistence.repository;

import com.ecommerce.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, UUID> {
    List<NotificationJpaEntity> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);
}
