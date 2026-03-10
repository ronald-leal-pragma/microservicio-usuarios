# Microservicio de Usuarios

Servicio Spring Boot para gestion de usuarios y autenticacion.

## Requisitos

- Java 21
- MySQL 8+
- Gradle Wrapper (incluido en el proyecto)

## Configuracion minima

La configuracion actual esta en `src/main/resources/application.yml`.

- Puerto: `8082`
- Base de datos: `usuarios_db` en `localhost:3306`
- Usuario DB: `root`
- Password DB: `admin`
- `ddl-auto`: `update`

Si necesitas otros valores, modifica `src/main/resources/application.yml` antes de iniciar.

## Levantar el servicio

### Windows (PowerShell)

```powershell
cd C:\Users\ronald.leal_pragma\Documents\microservicio-usuarios
.\gradlew.bat bootRun
```

### Linux/Mac

```bash
cd /ruta/al/proyecto/microservicio-usuarios
./gradlew bootRun
```

## Validar que inicio correctamente

- URL base: `http://localhost:8082`
- Swagger UI: `http://localhost:8082/swagger-ui/index.html`

## Comandos utiles

```powershell
# Ejecutar pruebas
.\gradlew.bat test

# Compilar el proyecto
.\gradlew.bat clean build
```

