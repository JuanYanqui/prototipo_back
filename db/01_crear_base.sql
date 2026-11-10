-- =====================================================================
-- PASO 1 - Ejecutar UNA sola vez conectado como superusuario (postgres)
-- a la base "postgres" (en DBeaver: Editor SQL > Ejecutar script, Alt+X).
--
-- Crea la base de datos "nova_app". La aplicacion se conecta con el
-- usuario postgres (ver src/main/resources/application.properties).
-- Si la base nova_app ya existe, no es necesario ejecutar este script.
-- =====================================================================

CREATE DATABASE nova_app
    WITH OWNER = postgres
    ENCODING = 'UTF8';
