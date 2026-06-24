package com.ecommerce.notification.interfaces.rest;

import com.ecommerce.notification.application.dto.command.UpdatePreferenceCommand;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.dto.response.PreferenceResponse;
import com.ecommerce.notification.application.port.in.ManagePreferenceUseCase;
import com.ecommerce.notification.application.port.in.QueryNotificationUseCase;
import com.ecommerce.notification.interfaces.rest.request.UpdatePreferenceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * API de notificaciones del usuario autenticado. El destinatario se toma del
 * token JWT, de modo que cada quien ve y gestiona solo sus propias notificaciones.
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notificaciones y preferencias del usuario autenticado")
public class NotificationController {

    private final QueryNotificationUseCase queryNotificationUseCase;
    private final ManagePreferenceUseCase managePreferenceUseCase;

    public NotificationController(QueryNotificationUseCase queryNotificationUseCase,
                                  ManagePreferenceUseCase managePreferenceUseCase) {
        this.queryNotificationUseCase = queryNotificationUseCase;
        this.managePreferenceUseCase = managePreferenceUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista las notificaciones del usuario autenticado")
    public ResponseEntity<List<NotificationResponse>> list(Authentication authentication) {
        UUID recipientId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(queryNotificationUseCase.getForRecipient(recipientId));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Marca una notificación como leída")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(queryNotificationUseCase.markAsRead(id));
    }

    @GetMapping("/preferences")
    @Operation(summary = "Obtiene las preferencias de notificación")
    public ResponseEntity<PreferenceResponse> getPreferences(Authentication authentication) {
        UUID recipientId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(managePreferenceUseCase.getPreferences(recipientId));
    }

    @PutMapping("/preferences")
    @Operation(summary = "Actualiza las preferencias de notificación")
    public ResponseEntity<PreferenceResponse> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody UpdatePreferenceRequest request) {
        UUID recipientId = UUID.fromString(authentication.getName());
        UpdatePreferenceCommand command = new UpdatePreferenceCommand(recipientId, request.channels());
        return ResponseEntity.ok(managePreferenceUseCase.update(command));
    }
}
