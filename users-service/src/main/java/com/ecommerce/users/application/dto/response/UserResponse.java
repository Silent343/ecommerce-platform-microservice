package com.ecommerce.users.application.dto.response;

import com.ecommerce.users.domain.model.aggregate.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * DTO de salida. Traduce el agregado User a una forma serializable hacia el exterior,
 * evitando exponer el modelo de dominio directamente.
 */
public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String role,
        boolean active,
        List<AddressResponse> addresses,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        List<AddressResponse> addresses = user.getAddresses().stream()
                .map(AddressResponse::from)
                .toList();
        return new UserResponse(
                user.getId().value(),
                user.getEmail().value(),
                user.getFullName().firstName(),
                user.getFullName().lastName(),
                user.getRole().name(),
                user.isActive(),
                addresses,
                user.getCreatedAt()
        );
    }
}
