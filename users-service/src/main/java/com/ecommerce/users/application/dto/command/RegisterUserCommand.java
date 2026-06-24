package com.ecommerce.users.application.dto.command;

/**
 * Command inmutable para registrar un usuario.
 * Transporta datos crudos desde la capa de interfaces hacia el servicio de aplicación.
 */
public record RegisterUserCommand(
        String email,
        String rawPassword,
        String firstName,
        String lastName,
        String role
) {
}
