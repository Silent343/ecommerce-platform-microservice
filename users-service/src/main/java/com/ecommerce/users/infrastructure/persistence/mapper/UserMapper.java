package com.ecommerce.users.infrastructure.persistence.mapper;

import com.ecommerce.users.domain.model.aggregate.User;
import com.ecommerce.users.domain.model.entity.Address;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.FullName;
import com.ecommerce.users.domain.model.valueobject.Password;
import com.ecommerce.users.domain.model.valueobject.Role;
import com.ecommerce.users.domain.model.valueobject.UserId;
import com.ecommerce.users.infrastructure.persistence.entity.AddressJpaEntity;
import com.ecommerce.users.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce entre el agregado de dominio (User) y el modelo de persistencia
 * (UserJpaEntity). Esta separación es clave en Clean Architecture: el dominio
 * permanece puro y la infraestructura adapta hacia/desde él.
 */
@Component
public class UserMapper {

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity(
                user.getId().value(),
                user.getEmail().value(),
                user.getPassword().hashedValue(),
                user.getFullName().firstName(),
                user.getFullName().lastName(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        List<AddressJpaEntity> addresses = new ArrayList<>();
        for (Address a : user.getAddresses()) {
            addresses.add(new AddressJpaEntity(
                    a.getAddressId(), a.getStreet(), a.getCity(), a.getRegion(),
                    a.getPostalCode(), a.getCountry(), a.isDefault(), entity));
        }
        entity.setAddresses(addresses);
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        List<Address> addresses = new ArrayList<>();
        for (AddressJpaEntity a : entity.getAddresses()) {
            addresses.add(new Address(
                    a.getId(), a.getStreet(), a.getCity(), a.getRegion(),
                    a.getPostalCode(), a.getCountry(), a.isDefault()));
        }

        return User.reconstitute(
                UserId.of(entity.getId()),
                Email.of(entity.getEmail()),
                Password.fromHash(entity.getPasswordHash()),
                FullName.of(entity.getFirstName(), entity.getLastName()),
                Role.valueOf(entity.getRole()),
                entity.isActive(),
                addresses,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
