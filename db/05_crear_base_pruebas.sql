-- =====================================================================
-- PASO 5 - Base de datos para las PRUEBAS AUTOMATICAS (mvn test).
-- Ejecutar UNA sola vez conectado como postgres a la base "postgres".
-- Es una base aparte para que las pruebas no ensucien nova_app.
-- Las tablas las crea la aplicacion al correr las pruebas (schema.sql).
-- =====================================================================

CREATE DATABASE nova_app_test
    WITH OWNER = postgres
    ENCODING = 'UTF8';
