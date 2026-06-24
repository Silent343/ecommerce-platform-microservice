-- Crea una base de datos por microservicio (patrón database-per-service).
-- Este script lo ejecuta automáticamente el contenedor de Postgres al iniciar
-- por primera vez (montado en /docker-entrypoint-initdb.d/).

CREATE DATABASE users_db;
CREATE DATABASE products_db;
CREATE DATABASE orders_db;
CREATE DATABASE payments_db;
CREATE DATABASE notif_db;

-- En producción, cada servicio tendría además su propio usuario con permisos
-- acotados solo a su base de datos. Para desarrollo local usamos el usuario
-- 'postgres' por defecto sobre todas.
