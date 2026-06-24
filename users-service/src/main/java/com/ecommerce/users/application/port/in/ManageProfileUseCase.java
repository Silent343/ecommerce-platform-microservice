package com.ecommerce.users.application.port.in;

import com.ecommerce.users.application.dto.command.AddAddressCommand;
import com.ecommerce.users.application.dto.command.UpdateProfileCommand;
import com.ecommerce.users.application.dto.response.UserResponse;

import java.util.UUID;

public interface ManageProfileUseCase {
    UserResponse getById(UUID userId);
    UserResponse updateProfile(UpdateProfileCommand command);
    UserResponse addAddress(AddAddressCommand command);
}
