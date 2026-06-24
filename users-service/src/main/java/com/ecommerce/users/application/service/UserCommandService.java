package com.ecommerce.users.application.service;

import com.ecommerce.users.application.dto.command.AddAddressCommand;
import com.ecommerce.users.application.dto.command.RegisterUserCommand;
import com.ecommerce.users.application.dto.command.UpdateProfileCommand;
import com.ecommerce.users.application.dto.response.UserResponse;
import com.ecommerce.users.application.port.in.DomainEventPublisher;
import com.ecommerce.users.application.port.in.ManageProfileUseCase;
import com.ecommerce.users.application.port.in.PasswordHasher;
import com.ecommerce.users.application.port.in.RegisterUserUseCase;
import com.ecommerce.users.domain.exception.EmailAlreadyExistsException;
import com.ecommerce.users.domain.exception.UserNotFoundException;
import com.ecommerce.users.domain.model.aggregate.User;
import com.ecommerce.users.domain.model.entity.Address;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.FullName;
import com.ecommerce.users.domain.model.valueobject.Password;
import com.ecommerce.users.domain.model.valueobject.Role;
import com.ecommerce.users.domain.model.valueobject.UserId;
import com.ecommerce.users.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación para los casos de uso de escritura (commands).
 * Orquesta el dominio, NO contiene reglas de negocio (esas viven en el agregado).
 * Su trabajo es coordinar: validar precondiciones, invocar el agregado,
 * persistir y publicar eventos.
 */
@Service
public class UserCommandService implements RegisterUserUseCase, ManageProfileUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final DomainEventPublisher eventPublisher;

    public UserCommandService(UserRepository userRepository,
                              PasswordHasher passwordHasher,
                              DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterUserCommand command) {
        Email email = Email.of(command.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.value());
        }

        Password.validatePolicy(command.rawPassword());
        Password password = Password.fromHash(passwordHasher.hash(command.rawPassword()));
        FullName fullName = FullName.of(command.firstName(), command.lastName());
        Role role = command.role() != null ? Role.valueOf(command.role()) : Role.CUSTOMER;

        User user = User.register(email, password, fullName, role);
        User saved = userRepository.save(user);

        eventPublisher.publishAll(user.pullDomainEvents());

        return UserResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(java.util.UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + userId));
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileCommand command) {
        User user = userRepository.findById(UserId.of(command.userId()))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + command.userId()));

        user.updateProfile(FullName.of(command.firstName(), command.lastName()));
        return UserResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse addAddress(AddAddressCommand command) {
        User user = userRepository.findById(UserId.of(command.userId()))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + command.userId()));

        Address address = Address.create(
                command.street(), command.city(), command.region(),
                command.postalCode(), command.country(), command.isDefault());

        user.addAddress(address);
        return UserResponse.from(userRepository.save(user));
    }
}
