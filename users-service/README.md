# users-service

Microservicio de **identidad y perfiles** de la plataforma e-commerce. Construido con Spring Boot 3 / Java 21 siguiendo **DDD**, **Clean Architecture** y **SOLID**.

## Arquitectura por capas

El flujo de dependencias siempre apunta hacia el dominio (regla de dependencia de Clean Architecture):

```
interfaces  ->  application  ->  domain  <-  infrastructure
```

- **domain**: el corazón. Agregado `User`, entidad `Address`, value objects (`Email`, `Password`, `UserId`, `FullName`, `Role`), eventos de dominio y el puerto `UserRepository`. No depende de ningún framework.
- **application**: casos de uso (`RegisterUserUseCase`, `AuthenticateUserUseCase`, `ManageProfileUseCase`), commands, DTOs de respuesta y servicios que orquestan el dominio. Define puertos de salida (`PasswordHasher`, `TokenProvider`, `DomainEventPublisher`).
- **infrastructure**: adaptadores concretos. JPA (entidades + mapper + `UserRepositoryImpl`), seguridad (BCrypt + JWT), mensajería (RabbitMQ) y config.
- **interfaces**: adaptadores de entrada REST (controllers, requests, manejo global de errores).

### Por qué esta separación

El dominio no conoce JPA, ni Spring Security, ni RabbitMQ. Eso permite testear la lógica de negocio sin levantar infraestructura, y cambiar de tecnología (ej: pasar de RabbitMQ a Kafka) tocando solo la capa de infraestructura.

## Stack

- Spring Boot 3.3 / Java 21
- Spring Data JPA + PostgreSQL
- Spring Security + JWT (jjwt) — **sin Lombok**
- RabbitMQ (eventos de dominio)
- Spring Cloud Netflix Eureka (service discovery)
- OpenAPI / Swagger UI

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registra un usuario |
| POST | `/api/auth/login` | Login, devuelve tokens JWT |
| POST | `/api/auth/refresh` | Renueva el access token |
| GET | `/api/users/{id}` | Obtiene un usuario |
| PUT | `/api/users/{id}/profile` | Actualiza perfil |
| POST | `/api/users/{id}/addresses` | Agrega dirección |

## Eventos publicados

- `user.registered` → exchange `users.exchange` (lo consume notification-service para el correo de bienvenida).

## Correr localmente

Requiere PostgreSQL y RabbitMQ. Variables de entorno relevantes: `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `RABBITMQ_HOST`, `JWT_SECRET`, `EUREKA_URL`.

```bash
mvn spring-boot:run
```

Swagger UI: http://localhost:8081/swagger-ui.html

## Docker

```bash
docker build -t users-service:1.0.0 .
```
