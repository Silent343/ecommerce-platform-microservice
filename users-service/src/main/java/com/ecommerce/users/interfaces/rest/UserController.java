package com.ecommerce.users.interfaces.rest;

import com.ecommerce.users.application.dto.command.AddAddressCommand;
import com.ecommerce.users.application.dto.command.UpdateProfileCommand;
import com.ecommerce.users.application.dto.response.UserResponse;
import com.ecommerce.users.application.port.in.ManageProfileUseCase;
import com.ecommerce.users.interfaces.rest.request.AddAddressRequest;
import com.ecommerce.users.interfaces.rest.request.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestión de perfiles y direcciones")
public class UserController {

    private final ManageProfileUseCase manageProfileUseCase;

    public UserController(ManageProfileUseCase manageProfileUseCase) {
        this.manageProfileUseCase = manageProfileUseCase;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un usuario por su ID")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(manageProfileUseCase.getById(id));
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Actualiza el perfil de un usuario")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(
                id, request.firstName(), request.lastName());
        return ResponseEntity.ok(manageProfileUseCase.updateProfile(command));
    }

    @PostMapping("/{id}/addresses")
    @Operation(summary = "Agrega una dirección al usuario")
    public ResponseEntity<UserResponse> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddAddressRequest request) {
        AddAddressCommand command = new AddAddressCommand(
                id, request.street(), request.city(), request.region(),
                request.postalCode(), request.country(), request.isDefault());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(manageProfileUseCase.addAddress(command));
    }
}
