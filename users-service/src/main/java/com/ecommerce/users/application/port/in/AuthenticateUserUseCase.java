package com.ecommerce.users.application.port.in;

import com.ecommerce.users.application.dto.command.LoginCommand;
import com.ecommerce.users.application.dto.response.AuthResponse;

public interface AuthenticateUserUseCase {
    AuthResponse login(LoginCommand command);
    AuthResponse refresh(String refreshToken);
}
