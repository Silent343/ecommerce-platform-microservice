package com.ecommerce.notification.application.port.in;

import com.ecommerce.notification.application.dto.command.UpdatePreferenceCommand;
import com.ecommerce.notification.application.dto.response.PreferenceResponse;

import java.util.UUID;

public interface ManagePreferenceUseCase {
    PreferenceResponse getPreferences(UUID recipientId);
    PreferenceResponse update(UpdatePreferenceCommand command);
}
