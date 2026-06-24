# ecommerce-platform

Plataforma de e-commerce con arquitectura de microservicios. Cada servicio es autónomo, con su propia base de datos, y se comunica de forma síncrona (REST vía Gateway) y asíncrona (eventos vía RabbitMQ).

## Estado actual

| Servicio | Puerto | Estado |
|----------|--------|--------|
| eureka-server | 8761 | ✅ listo |
| users-service | 8081 | ✅ listo |
| products-service | 8082 | ✅ listo |
| orders-service | 8083 | ✅ listo |
| payment-service | 8084 | ✅ listo |
| notification-service | 8085 | ✅ listo |
| api-gateway | 8080 | ✅ listo |

## Requisitos

Solo necesitas **Docker** y **Docker Compose**. No hace falta instalar Java, Maven, PostgreSQL ni RabbitMQ en tu máquina: todo se construye y corre en contenedores.

## Levantar todo

```bash
# 1. (opcional) copia las variables de entorno
cp .env.example .env

# 2. construye y levanta todo el stack
docker compose up --build
```

La primera vez tarda varios minutos porque compila cada servicio con Maven dentro de su contenedor. Las siguientes son mucho más rápidas.

Para correr en segundo plano:

```bash
docker compose up --build -d
```

Para ver los logs de un servicio:

```bash
docker compose logs -f users-service
```

Para apagar todo (sin borrar datos):

```bash
docker compose down
```

Para apagar y **borrar las bases de datos**:

```bash
docker compose down -v
```

## Verificar que levantó

Una vez arriba, revisa:

- **Eureka dashboard**: http://localhost:8761 → deberías ver `API-GATEWAY`, `USERS-SERVICE` y `PRODUCTS-SERVICE` registrados
- **RabbitMQ Management**: http://localhost:15672 (usuario `guest`, clave `guest`)
- **Swagger users**: http://localhost:8081/swagger-ui.html
- **Swagger products**: http://localhost:8082/swagger-ui.html

## Probar el flujo completo (vía gateway)

Todo entra por el **API Gateway en el puerto 8080**. El frontend solo necesita conocer esa dirección.

```bash
# 1. Registrar un vendedor
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "vendedor@test.com",
    "password": "Password123",
    "firstName": "Gabriel",
    "lastName": "Torres",
    "role": "SELLER"
  }'

# 2. Login -> copia el accessToken de la respuesta
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "vendedor@test.com", "password": "Password123" }'

# 3. Crear un producto (usa el token y el id del vendedor del paso 1)
TOKEN="pega-aqui-el-accessToken"
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sku": "LAPTOP-001",
    "name": "Laptop Gamer",
    "description": "16GB RAM, RTX 4060",
    "price": 4500.00,
    "currency": "PEN",
    "sellerId": "pega-aqui-el-id-del-vendedor",
    "initialStock": 10
  }'

# 4. Listar el catálogo (público, sin token)
curl http://localhost:8080/api/products
```

Si intentas el paso 3 **sin** el header `Authorization`, el gateway te corta con un `401` antes de siquiera llegar a products-service. Ese es el rechazo temprano en el borde.

## Orden de arranque

Docker Compose respeta las dependencias con healthchecks: primero `postgres` y `rabbitmq`, luego `eureka-server`, después los microservicios, y al final el `api-gateway`. Si un servicio arranca antes de tiempo, Compose espera a que sus dependencias estén sanas.
