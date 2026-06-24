# notification-service

Microservicio de **notificaciones**, el último eslabón del flujo. Spring Boot 3 / Java 21, DDD + Clean Architecture + SOLID. Es principalmente un **consumidor de eventos**: escucha lo que pasa en otros servicios y avisa al cliente.

## Qué escucha

- `order.confirmed` (de orders.exchange) → "¡Pedido confirmado!"
- `payment.completed` (de payments.exchange) → si el estado es `FAILED`, avisa "Problema con tu pago"

Muestra cómo un servicio puede suscribirse a eventos de **varios exchanges** distintos (orders y payment) sin acoplarse a esos servicios: solo conoce el contrato del evento.

## Modelo de dominio

- **Notification** (agregado): notificación con título, mensaje renderizado, canal y estado leído/no leído. Invariante simple: una vez leída, no vuelve a no-leída.
- **NotificationPreference** (agregado): canales que el usuario quiere (EMAIL, PUSH, SMS, IN_APP). Si no hay preferencias guardadas, usa un default razonable (EMAIL + IN_APP).
- **Value objects**: `NotificationId`, `RecipientId`, `NotificationType`, `NotificationChannel`.

## El envío detrás de un puerto

El envío real se abstrae con `NotificationSenderPort`. Hoy lo implementa `LogNotificationSender`, que registra en el log lo que se "enviaría". Para producción se reemplaza por **SendGrid** (email), **Firebase Cloud Messaging** (push) o **Twilio** (SMS), sin tocar el dominio. Mismo patrón hexagonal que la pasarela de pagos.

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/notifications` | Mis notificaciones |
| PUT | `/api/notifications/{id}/read` | Marcar como leída |
| GET | `/api/notifications/preferences` | Mis preferencias |
| PUT | `/api/notifications/preferences` | Actualizar preferencias |

El destinatario se toma del token JWT, así cada usuario ve solo lo suyo.

## Nota de diseño honesta

El evento `payment.completed` trae `orderId` pero no `customerId`. Para no inventar datos, en el caso de pago fallido el servicio usa el `orderId` como referencia de destinatario provisional. La mejora correcta sería enriquecer el evento de pago con el `customerId` (lo tiene orders al publicar `order.created`), o que notification consulte a orders-service por REST. Lo dejé documentado en el código como punto de extensión.

Swagger: http://localhost:8085/swagger-ui.html
