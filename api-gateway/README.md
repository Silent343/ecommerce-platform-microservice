# api-gateway

Punto de entrada único de la plataforma e-commerce, construido con **Spring Cloud Gateway** (reactivo, sobre WebFlux). Corre en el puerto **8080** y es la única puerta que el frontend necesita conocer.

## Qué hace

1. **Enrutamiento por descubrimiento**: resuelve los microservicios vía Eureka usando `lb://` (load balancer del lado cliente). El frontend nunca habla directo con los puertos 8081/8082; todo entra por el 8080.
2. **Validación JWT en el borde**: rechaza temprano (401) los tokens corruptos o expirados antes de que el tráfico llegue a los servicios. Cuando el token es válido, propaga la identidad con las cabeceras `X-User-Id` y `X-User-Role`.
3. **CORS centralizado**: configura CORS una sola vez acá para el frontend Angular (`http://localhost:4200`), en lugar de repetirlo en cada servicio.

## Tabla de rutas

| Ruta entrante | Servicio destino |
|---------------|------------------|
| `/api/auth/**` | users-service |
| `/api/users/**` | users-service |
| `/api/products/**` | products-service |
| `/api/categories/**` | products-service |

## Política de acceso en el borde

| Tipo de ruta | ¿Token? |
|--------------|---------|
| `/api/auth/**`, swagger, actuator | No requiere |
| `GET /api/products/**`, `GET /api/categories/**` | No requiere (catálogo público) |
| Todo lo demás | JWT válido obligatorio |
| `OPTIONS` (preflight CORS) | Siempre pasa |

La autorización fina por rol (quién puede crear productos, etc.) sigue viviendo en cada servicio: el gateway hace una primera barrera, y los servicios validan de nuevo (defensa en profundidad).

## Por qué Spring Cloud Gateway y no validar en cada servicio

Ambos. El gateway centraliza lo transversal (CORS, rechazo temprano de tokens basura, un solo origen para el frontend) y reduce la superficie expuesta a internet a un solo puerto. Cada microservicio mantiene su propia validación porque en una arquitectura distribuida no se confía ciegamente en que el tráfico siempre pasó por el gateway.

## Correr

Forma parte del `docker-compose.yml` raíz. Necesita que Eureka esté arriba para resolver las rutas `lb://`. Para correrlo solo:

```bash
mvn spring-boot:run
```

Una vez arriba, todo se consume por el 8080. Ejemplo:

```bash
# Antes: http://localhost:8081/api/auth/login
# Ahora (vía gateway):
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "gabriel@test.com", "password": "Password123" }'
```
