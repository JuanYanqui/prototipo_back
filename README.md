# Comercial Nova – Back (API REST)

API del prototipo del proyecto integrador (Lenguajes de Programación).
La interfaz está en el proyecto separado **prototipo_front** (JSF), que consume esta API.

```
prototipo_front (JSF, 8091)  ──REST/JSON──▶  prototipo_back (esta API, 8090)  ──▶  PostgreSQL
```

Resuelve los dos problemas críticos del caso:
1. **Se venden productos sin stock** → descuento de stock atómico en la base de datos y venta transaccional (todo o nada).
2. **Dos empleados modifican el mismo producto** → bloqueo optimista con columna `version`.

## Tecnologías
- Java 8 + Spring Boot 2.7 (Maven)
- JPA / Hibernate
- **PostgreSQL** (por defecto) · H2 en memoria (perfil `h2`, para demo sin servidor y para las pruebas)

## Base de datos PostgreSQL (DBeaver)
1. Si la base `nova_app` aún no existe: conectarse como `postgres` (base `postgres`) y ejecutar
   **`db/01_crear_base.sql`** → crea la base `nova_app`.
2. Conexión en DBeaver: puerto `5432`, base `nova_app`, usuario `postgres`.
3. *(Opcional)* Ejecutar **`db/02_tablas_y_datos.sql`** → tablas + datos de ejemplo.
   Si no se ejecuta, la API crea las tablas al arrancar (`schema.sql`) y carga los datos de ejemplo.

Conexión por defecto: `localhost:5432/nova_app`, usuario `postgres`. Se cambia en
`src/main/resources/application.properties` o con variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD`.

## Ejecutar
- NetBeans: abrir el proyecto → **Run (F6)**. La API queda en **http://localhost:8090/api/...**
- Luego ejecutar **prototipo_front** y abrir http://localhost:8091

Por línea de comandos:
```
mvn spring-boot:run                                   # contra PostgreSQL
mvn spring-boot:run -Dspring-boot.run.profiles=h2     # sin PostgreSQL (H2 en memoria)
mvn test                                              # pruebas en H2
mvn test -Dspring.profiles.active=pgtest              # pruebas contra PostgreSQL real
```

## Arquitectura (MVC por capas)
```
controller/   VentaController, ProductoController, ClienteController   ← reciben HTTP y responden JSON
              GlobalExceptionHandler                                    ← errores con formato único
service/      VentaService, ProductoService, ClienteService             ← reglas de negocio + transacciones
repository/   ProductoRepository, VentaRepository, ClienteRepository    ← acceso a datos
model/        Producto, Cliente, Venta, DetalleVenta                    ← entidades con sus reglas
dto/          Request / Response                                        ← lo que entra y sale de la API
```

## API
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista productos (con `version`) |
| GET | `/api/productos/{id}` | Un producto |
| POST | `/api/productos` | Crea producto |
| PUT | `/api/productos/{id}` | Edita; exige la `version` vista. 409 si cambió |
| GET | `/api/clientes` | Lista clientes |
| POST | `/api/ventas` | Registra venta. 201 ok · 409 sin stock · 400 datos inválidos |
| GET | `/api/ventas` | Últimas 20 ventas |

Ejemplo:
```json
POST /api/ventas
{ "clienteId": 1, "vendedor": "Vendedor 1",
  "items": [ { "productoId": 2, "cantidad": 3 } ] }
```
Errores (siempre el mismo formato):
```json
{ "status": 409, "error": "Conflict", "mensaje": "Stock insuficiente para 'Tablet Nova 10': disponible 0, solicitado 1", "fecha": "..." }
```

## Cómo se garantiza el stock
```sql
UPDATE producto SET stock = stock - :cantidad, version = version + 1
WHERE id = :id AND stock >= :cantidad
```
La comprobación y el descuento ocurren en **una sola sentencia**; si 20 vendedores compiten por la última
unidad, solo una actualización afecta 1 fila y las otras 19 afectan 0 → `StockInsuficienteException` y rollback.
Además la base tiene `CHECK (stock >= 0)`.

## Pruebas (12)
| Clase | Qué demuestra |
|---|---|
| `VentaServiceTest` | Venta válida, sin stock, todo-o-nada, cantidad 0, producto inexistente |
| `ConcurrenciaVentaTest` | 20 hilos / 1 unidad → 1 venta; 20 hilos / 5 unidades → 5 ventas |
| `ProductoEdicionTest` | Edición simultánea rechazada; venta durante edición invalida la edición |
| `VentaApiTest` | Recorrido HTTP completo: 201, 409, 400 |
