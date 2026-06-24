package com.ecommerce.notification.application.port.out;

import com.ecommerce.notification.domain.model.aggregate.Notification;

/**
 * Puerto de salida hacia el proveedor de envío (email, push, SMS).
 * Hoy lo implementa un adaptador que registra en log; en producción se
 * reemplaza por SendGrid, FCM, Twilio, etc., sin tocar el dominio.
 */
public interface NotificationSenderPort {
    void send(Notification notification);
}
