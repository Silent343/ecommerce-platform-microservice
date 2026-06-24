package com.ecommerce.users.domain.model.valueobject;

/**
 * Roles soportados dentro del bounded context de usuarios.
 * CUSTOMER: comprador final.
 * SELLER: vendedor que publica productos.
 * ADMIN: administrador de la plataforma.
 */
public enum Role {
    CUSTOMER,
    SELLER,
    ADMIN
}
