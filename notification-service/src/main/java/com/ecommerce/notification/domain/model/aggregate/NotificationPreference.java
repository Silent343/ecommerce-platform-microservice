package com.ecommerce.notification.domain.model.aggregate;

import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;

import java.util.EnumSet;
import java.util.Set;

/**
 * Preferencias de notificación de un usuario: por qué canales desea recibir
 * avisos. Si un usuario no tiene preferencias guardadas, se asume un default
 * razonable (EMAIL e IN_APP activos).
 */
public class NotificationPreference {

    private final RecipientId recipientId;
    private final Set<NotificationChannel> enabledChannels;

    private NotificationPreference(RecipientId recipientId, Set<NotificationChannel> enabledChannels) {
        this.recipientId = recipientId;
        this.enabledChannels = enabledChannels;
    }

    public static NotificationPreference defaultsFor(RecipientId recipientId) {
        return new NotificationPreference(recipientId,
                EnumSet.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP));
    }

    public static NotificationPreference of(RecipientId recipientId, Set<NotificationChannel> channels) {
        Set<NotificationChannel> safe = (channels == null || channels.isEmpty())
                ? EnumSet.of(NotificationChannel.IN_APP)
                : EnumSet.copyOf(channels);
        return new NotificationPreference(recipientId, safe);
    }

    public boolean isEnabled(NotificationChannel channel) {
        return enabledChannels.contains(channel);
    }

    /** Canal preferido para enviar: EMAIL si está activo, si no el primero disponible. */
    public NotificationChannel preferredChannel() {
        if (enabledChannels.contains(NotificationChannel.EMAIL)) {
            return NotificationChannel.EMAIL;
        }
        return enabledChannels.iterator().next();
    }

    public RecipientId getRecipientId() {
        return recipientId;
    }

    public Set<NotificationChannel> getEnabledChannels() {
        return EnumSet.copyOf(enabledChannels);
    }
}
