package com.ecommerce.notification.infrastructure.persistence.repository;

import com.ecommerce.notification.infrastructure.persistence.entity.PreferenceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferenceJpaRepository extends JpaRepository<PreferenceJpaEntity, UUID> {
}
