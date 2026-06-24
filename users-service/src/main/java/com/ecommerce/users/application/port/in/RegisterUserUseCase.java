package com.ecommerce.users.application.port.in;

import com.ecommerce.users.application.dto.command.RegisterUserCommand;
import com.ecommerce.users.application.dto.response.UserResponse;

/**
 * Puerto de entrada (input port). Define un caso de uso del sistema.
 * La capa de interfaces (controllers) depende de esta abstracción, no de la
 * implementación concreta. Esto mantiene el flujo de dependencias hacia el dominio.
 */
public interface RegisterUserUseCase {
    UserResponse register(RegisterUserCommand command);
}
