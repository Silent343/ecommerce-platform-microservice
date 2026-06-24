package com.ecommerce.notification.domain.model.aggregate;

import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;
import com.ecommerce.notification.domain.model.valueobject.NotificationId;
import com.ecommerce.notification.domain.model.valueobject.NotificationType;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;

import java.time.Instant;

/**
 * Agregado raíz de una notificación dirigida a un usuario. Guarda el contenido
 * ya renderizado, el canal por el que se envió y si fue leída. El invariante
 * principal es simple: una notificación leída no vuelve a "no leída".
 */
public class Notification {

    private final NotificationId id;
    private final RecipientId recipientId;
    private final NotificationType type;
    private final NotificationChannel channel;
    private final String title;
    private final String message;
    private boolean read;
    private final Instant createdAt;

    private Notification(NotificationId id, RecipientId recipientId, NotificationType type,
                         NotificationChannel channel, String title, String message,
                         boolean read, Instant createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.type = type;
        this.channel = channel;
        this.title = title;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public static Notification create(RecipientId recipientId, NotificationType type,
                                      NotificationChannel channel, String title, String message) {
        return new Notification(NotificationId.generate(), recipientId, type, channel,
                title, message, false, Instant.now());
    }

    public static Notification reconstitute(NotificationId id, RecipientId recipientId,
                                            NotificationType type, NotificationChannel channel,
                                            String title, String message, boolean read,
                                            Instant createdAt) {
        return new Notification(id, recipientId, type, channel, title, message, read, createdAt);
    }

    public void markAsRead() {
        this.read = true;
    }

    public NotificationId getId() {
        return id;
    }

    public RecipientId getRecipientId() {
        return recipientId;
    }

    public NotificationType getType() {
        return type;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
