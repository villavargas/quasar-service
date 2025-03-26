# Quasar Service

Este es un microservicio REST desarrollado en **Java Spring Boot** que permite determinar la posición y el mensaje transmitido por una nave espacial a partir de datos recibidos desde satélites.

##  Características
- API REST para procesar datos de satélites
- Cálculo de ubicación mediante trilateración
- Reconstrucción de mensajes incompletos
- Base de datos **H2** en memoria
- Documentación **Swagger/OpenAPI**
- Logs con **SLF4J + Logback**
- Pruebas unitarias e integrales con **JUnit 5 y MockMvc**
- Desplegable en **AWS** y **Google App Engine**

##  Estructura del Proyecto
```
quasar-service/
├── src/main/java/com/example/quasar
│   ├── controller/       # Controladores REST
│   ├── model/            # Modelos de datos
│   ├── repository/       # Persistencia con JPA
│   ├── service/          # Lógica de negocio
│   ├── exception/        # Manejo global de excepciones
│   ├── QuasarApplication.java  # Clase principal
├── src/main/resources
│   ├── application.properties  # Configuración del servicio
├── src/test/java/com/example/quasar
│   ├── controller/        # Pruebas de integración
│   ├── service/           # Pruebas unitarias
├── pom.xml               # Dependencias Maven
```

##  Instalación y Ejecución
### 1️⃣ Clonar el repositorio
```sh
git clone https://github.com/TU_REPOSITORIO/quasar-service.git
cd quasar-service
```
### 2️⃣ Construir el proyecto
```sh
mvn clean package -DskipTests
```
### 3️⃣ Ejecutar el servicio
```sh
java -jar target/quasar-service-1.0.0.jar
```
El servicio estará disponible en **http://localhost:8080**.

##  Documentación API (Swagger)
Una vez en ejecución, puedes acceder a **Swagger UI** en:
```
http://localhost:8080/swagger-ui/index.html
```

##  Endpoints Disponibles
### 1️⃣ **Obtener ubicación y mensaje en un solo request**
**POST /topsecret**
```json
{
  "satellites": [
    { "name": "kenobi", "distance": 100.0, "message": ["este", "", "", "mensaje", ""] },
    { "name": "skywalker", "distance": 115.5, "message": ["", "es", "", "", "secreto"] },
    { "name": "sato", "distance": 142.7, "message": ["este", "", "un", "", ""] }
  ]
}
```
**Respuesta:**
```json
{
  "position": { "x": -100.0, "y": 75.5 },
  "message": "este es un mensaje secreto"
}
```

### 2️⃣ **Enviar datos de satélites de forma separada**
**POST /topsecret_split/{satellite_name}**
```json
{
  "distance": 100.0,
  "message": ["este", "", "", "mensaje", ""]
}
```
**Respuesta:** `201 Created`

### 3️⃣ **Obtener ubicación y mensaje cuando los datos están completos**
**GET /topsecret_split**
**Respuesta:**
```json
{
  "position": { "x": -100.0, "y": 75.5 },
  "message": "este es un mensaje secreto"
}
```

## 🧪 Pruebas
Para ejecutar las pruebas unitarias e integrales:
```sh
mvn test
```

## ☁️ Despliegue en AWS
1. Construir la imagen Docker:
   ```sh
   docker build -t quasar-service .
   ```
2. Ejecutar el contenedor:
   ```sh
   docker run -d -p 8080:8080 --name quasar-container quasar-service
   ```



## 📄 Licencia
Este proyecto está bajo la licencia **MIT**.

