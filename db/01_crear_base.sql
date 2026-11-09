-- =====================================================================
-- PASO 1 - Ejecutar UNA sola vez conectado como superusuario (postgres)
-- a la base "postgres" (en DBeaver: Editor SQL > Ejecutar script, Alt+X).
--
-- Crea un usuario propio para la aplicacion y la base de datos.
-- Cambie la clave si la base no es solo para pruebas.
-- =====================================================================

CREATE ROLE nova_app WITH LOGIN PASSWORD 'nova123';

CREATE DATABASE comercialnova
    WITH OWNER = nova_app
    ENCODING = 'UTF8';
