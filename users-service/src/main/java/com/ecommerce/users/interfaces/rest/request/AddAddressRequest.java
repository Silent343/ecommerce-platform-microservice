package com.ecommerce.users.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record AddAddressRequest(
        @NotBlank String street,
        @NotBlank String city,
        String region,
        String postalCode,
        String country,
        boolean isDefault
) {
}
