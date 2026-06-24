package com.ecommerce.users.domain.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa una contraseña YA hasheada.
 * El dominio nunca almacena ni transporta la contraseña en texto plano.
 * El hashing real lo realiza un PasswordEncoder en la capa de aplicación/infraestructura;
 * aquí solo se garantiza el invariante de que el hash no sea vacío.
 */
public final class Password {

    private final String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    public static Password fromHash(String hashedValue) {
        if (hashedValue == null || hashedValue.isBlank()) {
            throw new IllegalArgumentException("El hash de la contraseña no puede estar vacío");
        }
        return new Password(hashedValue);
    }

    /**
     * Valida la política de contraseñas sobre el texto plano ANTES de hashear.
     * Se invoca desde la capa de aplicación.
     */
    public static void validatePolicy(String raw) {
        if (raw == null || raw.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!raw.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos una mayúscula");
        }
        if (!raw.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos un número");
        }
    }

    public String hashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password password)) return false;
        return hashedValue.equals(password.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "Password{****}";
    }
}
