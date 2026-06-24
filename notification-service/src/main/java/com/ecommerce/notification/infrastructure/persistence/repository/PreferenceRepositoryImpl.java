package com.ecommerce.notification.infrastructure.persistence.repository;

import com.ecommerce.notification.domain.model.aggregate.NotificationPreference;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;
import com.ecommerce.notification.domain.repository.PreferenceRepository;
import com.ecommerce.notification.infrastructure.persistence.mapper.PreferenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PreferenceRepositoryImpl implements PreferenceRepository {

    private final PreferenceJpaRepository jpaRepository;
    private final PreferenceMapper mapper;

    public PreferenceRepositoryImpl(PreferenceJpaRepository jpaRepository, PreferenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public NotificationPreference save(NotificationPreference preference) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(preference)));
    }

    @Override
    public Optional<NotificationPreference> findByRecipient(RecipientId recipientId) {
        return jpaRepository.findById(recipientId.value()).map(mapper::toDomain);
    }
}
