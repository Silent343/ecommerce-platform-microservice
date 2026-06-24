package com.ecommerce.users.application.port.in;

import com.ecommerce.users.domain.model.aggregate.User;

/**
 * Puerto de salida para la generación y validación de tokens.
 */
public interface TokenProvider {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractSubject(String token);
    boolean isValid(String token);
    long accessTokenTtlSeconds();
}
