# products-service

Microservicio de **catálogo e inventario** de la plataforma e-commerce. Spring Boot 3 / Java 21, DDD + Clean Architecture + SOLID. Misma arquitectura por capas que users-service.

## Modelo de dominio

- **Product** (agregado raíz): encapsula datos del producto, precio (`Money`), inventario (`Stock`) e imágenes. Protege invariantes como stock no negativo y una sola imagen principal.
- **Category** (agregado): agrupa productos. Se relaciona con Product por ID, no por composición.
- **Value objects**: `ProductId`, `CategoryId`, `Sku`, `Money` (BigDecimal + moneda, default PEN), `Stock` (operaciones inmutables), `SellerId`.

### Decisión clave: referencias entre servicios por ID

El vendedor (`SellerId`) es un usuario con rol SELLER que vive en **users-service**. Acá se guarda **solo su UUID**, nunca una FK entre bases de datos. Cada microservicio es dueño exclusivo de sus datos; las relaciones cruzadas se resuelven por ID y, si hace falta enriquecer, vía llamadas REST o eventos.

## CQRS ligero

Las escrituras (`ProductCommandService`) y lecturas (`ProductQueryService`) están separadas en servicios distintos, anticipando que el catálogo se lee muchísimo más de lo que se escribe.

## Endpoints

| Método | Ruta | Acceso |
|--------|------|--------|
| GET | `/api/products` | Público (filtra por `?categoryId=`) |
| GET | `/api/products/{id}` | Público |
| POST | `/api/products` | SELLER / ADMIN |
| PUT | `/api/products/{id}` | SELLER / ADMIN |
| GET | `/api/products/{id}/stock` | Público |
| PUT | `/api/products/{id}/stock` | SELLER / ADMIN |
| GET | `/api/categories` | Público |
| POST | `/api/categories` | ADMIN |

El ajuste de stock acepta `operation`: `SET`, `INCREASE` o `DECREASE`.

## Eventos publicados

- `product.created` → exchange `products.exchange`
- `stock.updated` → exchange `products.exchange`

## Seguridad

Este servicio **valida** tokens JWT pero **no los emite** (el login vive en users-service). Comparte el mismo `JWT_SECRET`. Los GET son públicos; las escrituras exigen rol. Es defensa en profundidad: aunque el API Gateway valide, cada servicio vuelve a verificar.

## Correr

Forma parte del `docker-compose.yml` raíz. Para correrlo solo:

```bash
mvn spring-boot:run
```

Swagger: http://localhost:8082/swagger-ui.html
