package com.ecommerce.notification.infrastructure.sender;

import com.ecommerce.notification.application.port.out.NotificationSenderPort;
import com.ecommerce.notification.domain.model.aggregate.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementación SIMULADA del envío de notificaciones: registra en el log lo
 * que se "enviaría". En producción se reemplaza por SendGrid (email),
 * Firebase Cloud Messaging (push) o Twilio (SMS), sin tocar el dominio.
 */
@Component
public class LogNotificationSender implements NotificationSenderPort {

    private static final Logger log = LoggerFactory.getLogger(LogNotificationSender.class);

    @Override
    public void send(Notification notification) {
        log.info("[{}] -> destinatario {} | {} : {}",
                notification.getChannel(),
                notification.getRecipientId(),
                notification.getTitle(),
                notification.getMessage());
    }
}
