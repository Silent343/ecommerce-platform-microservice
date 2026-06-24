# orders-service

Microservicio de **carrito y pedidos**, el núcleo del negocio. Spring Boot 3 / Java 21, DDD + Clean Architecture + SOLID. Es el servicio más interesante porque combina los **dos estilos de comunicación entre microservicios**.

## Los dos estilos de comunicación

### Síncrono (REST vía Eureka)
Al agregar al carrito y al hacer checkout, orders consulta a **products-service** en tiempo real (precio, stock) usando un `RestClient` con balanceo de carga (`http://PRODUCTS-SERVICE`, resuelto por Eureka). Vive en `ProductCatalogAdapter`, detrás del puerto `ProductCatalogPort`: el dominio no sabe que hay un HTTP de por medio.

### Asíncrono (eventos RabbitMQ)
- **Publica** `order.created` (lo consume payment-service) y `order.confirmed` (lo consume notification-service).
- **Escucha** `payment.completed` desde payment-service, en `PaymentEventListener`.

## El flujo completo de un pedido

1. Cliente hace `POST /api/orders/checkout`.
2. orders verifica stock de cada ítem contra products-service (**síncrono**).
3. Crea el pedido en estado `PENDING` y vacía el carrito.
4. Publica `order.created` → payment-service lo toma y cobra.
5. payment-service publica `payment.completed`.
6. `PaymentEventListener` lo recibe:
   - si `COMPLETED` → confirma el pedido, descuenta stock en products (**síncrono**), publica `order.confirmed`.
   - si `FAILED` → marca el pedido como `PAYMENT_FAILED`.
7. notification-service recibe `order.confirmed` y avisa al cliente.

Los pasos 4, 5 y 7 son asíncronos: no bloquean la respuesta HTTP del checkout.

## Modelo de dominio

- **Cart** (agregado): un carrito por cliente. Acumula cantidades del mismo producto en lugar de duplicar líneas.
- **Order** (agregado): máquina de estados `PENDING → CONFIRMED | PAYMENT_FAILED | CANCELLED`. Rechaza transiciones desde estados finales. Sus `OrderItem` son inmutables: congelan precio y cantidad al checkout, así cambios en el catálogo no alteran pedidos pasados.
- **Value objects**: `OrderId`, `CartId`, `CustomerId`, `Money` (con `add`/`multiply`), `OrderStatus`.

## Seguridad

Toda la API es privada (requiere JWT válido). El `customerId` se toma del **subject del token**, nunca de un parámetro, así un cliente solo puede operar su propio carrito y ver sus propios pedidos.

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/cart` | Carrito del cliente autenticado |
| POST | `/api/cart/items` | Agrega producto al carrito |
| DELETE | `/api/cart/items/{itemId}` | Quita ítem |
| POST | `/api/orders/checkout` | Genera el pedido |
| GET | `/api/orders` | Mis pedidos |
| GET | `/api/orders/{id}` | Detalle de pedido |
| PUT | `/api/orders/{id}/cancel` | Cancela (solo si está PENDING) |

## Nota sobre payment-service

orders ya declara la cola `payment.completed.orders.queue` ligada a `payments.exchange`. Como payment-service todavía no existe, los pedidos se quedan en `PENDING` hasta que lo construyamos. Para probar el flujo completo mientras tanto, se puede publicar manualmente un `payment.completed` desde la consola de RabbitMQ.

Swagger: http://localhost:8083/swagger-ui.html
