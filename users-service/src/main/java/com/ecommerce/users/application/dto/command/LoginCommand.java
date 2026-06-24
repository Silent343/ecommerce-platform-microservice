package com.ecommerce.users.application.dto.command;

public record LoginCommand(
        String email,
        String rawPassword
) {
}
