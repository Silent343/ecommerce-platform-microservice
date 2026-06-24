package com.ecommerce.users.infrastructure.persistence.repository;

import com.ecommerce.users.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA. Es un detalle de infraestructura, por eso vive
 * aquí y no en el dominio. Trabaja con UserJpaEntity, no con el agregado.
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
