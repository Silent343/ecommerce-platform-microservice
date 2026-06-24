package com.ecommerce.users.application.service;

import com.ecommerce.users.application.dto.command.LoginCommand;
import com.ecommerce.users.application.dto.response.AuthResponse;
import com.ecommerce.users.application.port.in.AuthenticateUserUseCase;
import com.ecommerce.users.application.port.in.PasswordHasher;
import com.ecommerce.users.application.port.in.TokenProvider;
import com.ecommerce.users.domain.exception.InvalidCredentialsException;
import com.ecommerce.users.domain.exception.UserNotFoundException;
import com.ecommerce.users.domain.model.aggregate.User;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.UserId;
import com.ecommerce.users.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación para autenticación. Valida credenciales contra el
 * agregado User y delega la emisión de tokens al TokenProvider.
 */
@Service
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordHasher passwordHasher,
                                 TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginCommand command) {
        Email email = Email.of(command.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.isActive()) {
            throw new InvalidCredentialsException();
        }

        boolean matches = passwordHasher.matches(
                command.rawPassword(), user.getPassword().hashedValue());
        if (!matches) {
            throw new InvalidCredentialsException();
        }

        return issueTokens(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refresh(String refreshToken) {
        if (!tokenProvider.isValid(refreshToken)) {
            throw new InvalidCredentialsException();
        }
        String subject = tokenProvider.extractSubject(refreshToken);
        User user = userRepository.findById(UserId.of(subject))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + subject));

        return issueTokens(user);
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        return AuthResponse.bearer(accessToken, refreshToken, tokenProvider.accessTokenTtlSeconds());
    }
}
