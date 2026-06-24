package com.ecommerce.notification.interfaces.rest.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdatePreferenceRequest(
        @NotEmpty Set<String> channels
) {
}
