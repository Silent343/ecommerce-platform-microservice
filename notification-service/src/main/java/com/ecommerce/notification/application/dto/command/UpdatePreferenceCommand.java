package com.ecommerce.notification.application.dto.command;

import java.util.Set;
import java.util.UUID;

public record UpdatePreferenceCommand(
        UUID recipientId,
        Set<String> channels
) {
}
