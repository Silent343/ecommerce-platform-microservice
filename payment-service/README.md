# payment-service

Microservicio de **pagos y reembolsos**. Spring Boot 3 / Java 21, DDD + Clean Architecture + SOLID. Es el que **cierra el flujo del pedido**: escucha `order.created`, cobra, y publica `payment.completed`, que orders-service consume para confirmar (o rechazar) el pedido.

## El cierre del loop

```
orders-service  --order.created-->   payment-service
                                          | (cobra vía pasarela)
orders-service  <--payment.completed--   payment-service
       |
   confirma el pedido (CONFIRMED) o lo marca PAYMENT_FAILED
```

Apenas este servicio está arriba, un checkout en orders-service avanza solo hasta `CONFIRMED`, sin intervención manual.

## Modelo de dominio

- **Payment** (agregado raíz): máquina de estados `PENDING → COMPLETED → REFUNDED`, o `PENDING → FAILED`. Al completarse genera un `Receipt` (comprobante) y acumula el evento `payment.completed`.
- **Refund** (entidad): reembolso de un pago ya completado.
- **Value objects**: `PaymentId`, `OrderRef`, `CustomerId`, `Money`, `PaymentStatus`, `PaymentMethod` (incluye **Yape** y **Plin**, las billeteras móviles peruanas), `Receipt`.

## La pasarela detrás de un puerto

El cobro real se abstrae con `PaymentGatewayPort`. Hoy lo implementa `SimulatedPaymentGateway` (aprueba el cobro y genera una referencia ficticia). Para producción se reemplaza por Culqi, Niubiz o Stripe **sin tocar el dominio ni la aplicación**: solo se cambia ese adaptador. Esa es la razón de ser del puerto.

## Dos formas de cobrar

1. **Automática (evento)**: `OrderEventListener` escucha `order.created` y dispara el cobro con método CARD por defecto. Es lo que cierra el flujo del pedido.
2. **Manual (REST)**: `POST /api/payments/process` permite procesar un pago indicando el método (útil si en el futuro hay una UI de checkout donde el cliente elige Yape, tarjeta, etc.).

## Endpoints

| Método | Ruta | Acceso |
|--------|------|--------|
| POST | `/api/payments/process` | Autenticado |
| GET | `/api/payments/{id}` | Autenticado |
| GET | `/api/payments/order/{orderId}` | Autenticado |
| GET | `/api/payments/{id}/receipt` | Autenticado |
| POST | `/api/payments/{id}/refund` | ADMIN |

## Eventos

- **Escucha**: `order.created` (de orders.exchange) → cola `order.created.payments.queue`
- **Publica**: `payment.completed` (a payments.exchange), con `status` = `COMPLETED` o `FAILED`

Swagger: http://localhost:8084/swagger-ui.html
