package com.ecommerce.users.interfaces.rest;

import com.ecommerce.users.application.dto.command.LoginCommand;
import com.ecommerce.users.application.dto.command.RegisterUserCommand;
import com.ecommerce.users.application.dto.response.AuthResponse;
import com.ecommerce.users.application.dto.response.UserResponse;
import com.ecommerce.users.application.port.in.AuthenticateUserUseCase;
import com.ecommerce.users.application.port.in.RegisterUserUseCase;
import com.ecommerce.users.interfaces.rest.request.LoginRequest;
import com.ecommerce.users.interfaces.rest.request.RefreshRequest;
import com.ecommerce.users.interfaces.rest.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de autenticación. Es un adaptador de entrada (input adapter):
 * traduce HTTP a commands y delega en los casos de uso. No contiene lógica
 * de negocio.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Registro y autenticación de usuarios")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Registra un nuevo usuario")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.email(), request.password(),
                request.firstName(), request.lastName(), request.role());
        UserResponse response = registerUserUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica un usuario y devuelve tokens JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authenticateUserUseCase.login(
                new LoginCommand(request.email(), request.password()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renueva el access token usando un refresh token")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthResponse response = authenticateUserUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
