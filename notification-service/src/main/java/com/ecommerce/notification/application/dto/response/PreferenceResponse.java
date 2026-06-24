package com.ecommerce.notification.application.dto.response;

import com.ecommerce.notification.domain.model.aggregate.NotificationPreference;
import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record PreferenceResponse(
        UUID recipientId,
        Set<String> enabledChannels
) {
    public static PreferenceResponse from(NotificationPreference p) {
        Set<String> channels = p.getEnabledChannels().stream()
                .map(NotificationChannel::name)
                .collect(Collectors.toSet());
        return new PreferenceResponse(p.getRecipientId().value(), channels);
    }
}
