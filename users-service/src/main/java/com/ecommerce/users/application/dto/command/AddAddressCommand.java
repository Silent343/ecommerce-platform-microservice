package com.ecommerce.users.application.dto.command;

import java.util.UUID;

public record AddAddressCommand(
        UUID userId,
        String street,
        String city,
        String region,
        String postalCode,
        String country,
        boolean isDefault
) {
}
