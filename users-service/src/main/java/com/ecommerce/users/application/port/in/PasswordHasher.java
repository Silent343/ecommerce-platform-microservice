package com.ecommerce.users.application.port.in;

/**
 * Puerto de salida para el hashing de contraseñas. La implementación concreta
 * (BCrypt) vive en infrastructure/security, así el dominio y la aplicación
 * no dependen de Spring Security.
 */
public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}
