package com.ecommerce.users.domain.repository;

import com.ecommerce.users.domain.model.aggregate.User;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.UserId;

import java.util.Optional;

/**
 * Puerto de salida (output port). Define el contrato de persistencia en términos
 * del dominio, SIN conocer JPA ni la base de datos. La implementación concreta
 * vive en infrastructure/persistence. Esto invierte la dependencia (SOLID - DIP):
 * el dominio no depende de la infraestructura, la infraestructura depende del dominio.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void deleteById(UserId id);
}
