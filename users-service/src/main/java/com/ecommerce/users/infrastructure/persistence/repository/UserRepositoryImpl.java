package com.ecommerce.users.infrastructure.persistence.repository;

import com.ecommerce.users.domain.model.aggregate.User;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.UserId;
import com.ecommerce.users.domain.repository.UserRepository;
import com.ecommerce.users.infrastructure.persistence.entity.UserJpaEntity;
import com.ecommerce.users.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador de persistencia (output adapter). Implementa el puerto del dominio
 * UserRepository delegando en Spring Data JPA y traduciendo con UserMapper.
 *
 * Aquí se materializa la inversión de dependencias (DIP): el dominio define la
 * interfaz, la infraestructura la implementa.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.value());
    }
}
