package com.ecommerce.notification.infrastructure.persistence.repository;

import com.ecommerce.notification.domain.model.aggregate.Notification;
import com.ecommerce.notification.domain.model.valueobject.NotificationId;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import com.ecommerce.notification.infrastructure.persistence.mapper.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationMapper mapper;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository, NotificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(notification)));
    }

    @Override
    public Optional<Notification> findById(NotificationId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Notification> findByRecipient(RecipientId recipientId) {
        return jpaRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId.value()).stream()
                .map(mapper::toDomain).toList();
    }
}
