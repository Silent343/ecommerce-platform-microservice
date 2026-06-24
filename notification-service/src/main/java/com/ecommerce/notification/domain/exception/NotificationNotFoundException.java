package com.ecommerce.notification.domain.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String id) {
        super("Notificación no encontrada: " + id);
    }
}
