# Proyecto API Usuarios – Spring Boot

## Descripción
Este proyecto es una API REST desarrollada en Java 17 con Spring Boot, 
diseñada para la creación y gestión básica de usuarios, 
cumpliendo validaciones de negocio, manejo de errores estandarizado y 
documentación OpenAPI.

**Funcionalidades clave:**

- Creación de usuarios mediante API REST.
- Validaciones de entrada con Bean Validation.
- Control de duplicidad de correo electrónico.
- Manejo centralizado de excepciones.
- Persistencia de datos con Spring Data JPA.
- Base de datos H2 en memoria.
- Documentación automática con Swagger/OpenAPI.
- Pruebas unitarias con JUnit y Mockito.
- Contenerización con Docker.
- Preparado para despliegue en Kubernetes.

---

## Arquitectura
```
Cliente (Front / Postman / cURL)
|
v
REST Controller
|
v
Service Layer
|
v
Repository / JPA
|
v
Base de Datos
```

- **Controller:** Expone los endpoints REST.
- **Service:** Contiene la lógica de negocio.
- **Repository:** Acceso a datos mediante Spring Data JPA
- **Entity / DTO:** Separación entre modelo de persistencia y transporte.
- **ControllerAdvice:** Manejo global de errores.

---

## Tecnologías

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.x
- **Persistencia:** Spring Data JPA / Hibernate
- **Base de Datos:** H2 Database (in-memory)
- **Contenerización:** Docker
- **Orquestación:** Kubernetes
- **Testing:** JUnit, Mockito
- **Documentación API:** Swagger/OpenAPI
- **Logging:** SLF4J + Lombok
- **Build Tool:** Maven

---

## Requisitos previos

- Java 17
- Maven 3.x
- Docker (opcional)
- Kubernetes (opcional)
- `kubectl` configurado (opcional)
- Base de datos accesible
> Nota: H2 Database se ejecuta en memoria y no requiere configuración externa. La base de datos se reinicia cada vez que la aplicación se detiene.
---

## Instalación y ejecución local

1. **Clonar el repositorio**
```bash
git clone <url-del-repo>
cd nombre-del-proyecto
```

2. **Construir proyecto**
```bash
mvn clean install
```

3. **Ejecutar localmente**
```bash
mvn spring-boot:run
```

4. **Acceder a la consola H2 (para ver tablas y datos):**

- http://localhost:8080/h2-console

- JDBC URL: jdbc:h2:mem:bci-db
- Usuario: sa
- Contraseña: (vacía por defecto)

5. **Swagger UI para probar endpoints:**

- http://localhost:8080/swagger-ui/index.html

---
# Endpoints disponibles
| Método | URL             | Descripción   |
| ------ | --------------- | ------------- |
| POST   | `/api/v1/users` | Crear usuario |

---
# Ejemplo de request (cURL)
```
curl -X POST http://localhost:8080/api/v1/users \
-H "Content-Type: application/json" \
-d '{
          "name": "Rene Alarcon",
          "email": "ralarcon@bci.cl",
          "password": "Password123",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "contrycode": "57"
            },
            {
              "number": "7654321",
              "citycode": "2",
              "contrycode": "56"
            }
          ]
        }'
```
---
## Respuestas HTTP
| Código | Descripción                  |
| ------ | ---------------------------- |
| 201    | Usuario creado correctamente |
| 400    | Datos inválidos              |
| 409    | Correo ya registrado         |
| 500    | Error interno del sistema    |

- **Ejemplo de error:**
```
{
  "mensaje": "El correo ya registrado"
}
```
---
## Manejo de errores
La aplicación utiliza un GlobalExceptionHandler con @RestControllerAdvice para:

- Validaciones de entrada (@Valid)
- Errores de negocio (correo duplicado)
- Errores inesperados
Esto garantiza respuestas claras y consistentes para el cliente.

---
## Testing
- **Ejecutar test:**
```
mvn test
```
---
## Docker
**Construir imagen**
```bash
docker build -t miapp-java:1.0 .
```

**Ejecutar contenedor**
```bash
docker run -p 8080:8080 users-api:1.0
``` 
```
Comandos útiles
docker ps               # listar contenedores activos
docker images           # listar imágenes locales
docker rm <container>   # eliminar contenedor
docker rmi <image>      # eliminar imagen
```
---
## Docker Compose
**Construir y ejecutar la aplicacion**
- Desde la raíz del proyecto, ejecutar:
```bash
docker compose up --build
```
**Esto realizará:**
- Construcción de la imagen Docker (bci-rene:1.0)
- Ejecución de la aplicación Spring Boot
- Exposición del servicio en http://localhost:8080

**Archivo docker-compose.yml**
```
version: "3.9"
services:
  app:
    build: .
    image: bci-rene:1.0
    ports:
      - "8080:8080"
```
**Detener la aplicación**
```bash
docker compose down
```
**Notas**
- La imagen se construye automáticamente desde el Dockerfile.
- La aplicación se ejecuta usando Java 17.
- > ℹ️ En versiones recientes de Docker, Docker Compose se ejecuta como `docker compose`.

**Verificar que lo tienes instalado**
```bash
docker compose version
```
---
# Kubernetes
## Manifiestos

- deployment.yaml – Deployment con replicas y estrategia de actualización
- service.yaml – Servicio ClusterIP / LoadBalancer
- configmap.yaml – Configuración de la aplicación
- secret.yaml – Secrets de base de datos

## Despliegue
```bash
kubectl apply -f k8s/
```

## Verificación
```bash
kubectl get pods
kubectl get svc
kubectl logs <pod-name>
```
---

## Buenas prácticas aplicadas
- Separación de capas (Controller / Service / Repository)
- DTOs para entrada y salida
- Validaciones con Bean Validation
- Manejo centralizado de excepciones
- Mensajes de error claros y consistentes
- Documentación OpenAPI clara y alineada a los códigos HTTP
- Código limpio y mantenible
---

## Autor
- René Alarcón




