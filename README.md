# Flight App ✈️

Sistema de gestión y reserva de vuelos desarrollado con Spring Boot, implementando una arquitectura de microservicios.

## 📋 Descripción

Flight App es una aplicación web completa para la gestión de vuelos y reservas, que consta de dos módulos principales:

- **flight-web**: Aplicación web principal con interfaz de usuario
- **file-store-api**: API REST para gestión de archivos y recursos

## 🏗️ Arquitectura

```
flight-app/
├── flight-web/          # Aplicación web principal
│   ├── Controllers MVC  # Controladores para vistas Thymeleaf
│   ├── Services        # Lógica de negocio
│   ├── Repositories    # Acceso a datos con JPA
│   └── Security        # Configuración de seguridad
│
└── file-store-api/     # API de almacenamiento
    ├── REST Controllers # Endpoints API REST
    ├── JWT Security    # Autenticación basada en tokens
    └── File Storage    # Sistema de almacenamiento local
```

## 🔧 Requisitos Previos

- **Java 21** o superior
- **Maven 3.9+**
- **MariaDB** (o MySQL)
- **Puerto 8080** disponible para flight-web
- **Puerto 8081** disponible para file-store-api

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone [url-del-repositorio]
cd flight-app
```

### 2. Configurar la base de datos

#### Usando Docker Compose (recomendado):

```bash
docker-compose -f docker-compose-dev.yml up -d
```

#### O configurar MariaDB manualmente:

1. Crear base de datos:
```sql
CREATE DATABASE flights;
CREATE DATABASE db;
```

2. Ejecutar scripts de inicialización (ubicados en `flight-web/src/main/resources/scripts/database/`):
```sql
-- Ejecutar en orden:
001-upgrade.sql  -- Crea tabla de aeropuertos
002-upgrade.sql  -- Crea tablas de usuarios y roles
003-upgrade.sql  -- Crea tablas de vuelos y reservas
```

### 3. Configurar application.yml

#### flight-web (`flight-web/src/main/resources/application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/flights
    username: root
    password: dbpw
    
  mail:
    host: smtp.gmail.com
    port: 587
    username: tu-email@gmail.com
    password: tu-contraseña-de-aplicación
```

#### file-store-api (`file-store-api/src/main/resources/application.yml`):

```yaml
application:
  store:
    base-path: C:\ruta\a\tu\directorio\archivos
```

### 4. Compilar el proyecto

```bash
mvn clean install
```

### 5. Ejecutar las aplicaciones

#### Terminal 1 - File Store API:
```bash
cd file-store-api
mvn spring-boot:run
```

#### Terminal 2 - Flight Web:
```bash
cd flight-web
mvn spring-boot:run
```

## 🎯 Funcionalidades Principales

### Flight Web (Puerto 8080)

#### 🔐 Autenticación y Autorización
- **Login**: `/login`
- Usuarios predefinidos:
    - **Usuario estándar**: `user@bla.com` / password: `user`
    - **Administrador**: `admin@bla.com` / password: `admin`

#### ✈️ Gestión de Vuelos
- **Listado de vuelos**: `/flight/flights`
- **Crear/Editar vuelo**: `/flight/flights-edit` (solo ADMIN)
- **Reservar vuelo**: `/flight/bookings-add/{flightId}`
- **Ver reservas**: `/flight/bookings`

#### 🌍 Internacionalización
- Soporte para Español e Inglés
- Cambio de idioma mediante parámetro `?lang=es` o `?lang=en`

#### 📧 Sistema de Notificaciones
- Envío de emails con información de vuelos
- Adjuntos con imágenes de vuelos

### File Store API (Puerto 8081)

#### 📁 Gestión de Recursos
- **Base URL**: `http://localhost:8081`
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`

#### 🔑 Autenticación JWT
- Usuarios predefinidos:
    - **consumer**: Solo lectura (`read-resource`)
    - **producer**: Lectura y escritura (`read-resource`, `write-resource`)
    - Password para ambos: `password`

#### 📚 API de Ejemplo - Libros
- `GET /base/api/books` - Listar libros
- `GET /base/api/books/{id}` - Obtener libro por ID
- `POST /base/api/books` - Crear libro
- `PUT /base/api/books/{id}` - Actualizar libro
- `DELETE /base/api/books/{id}` - Eliminar libro

## 📊 Modelo de Datos

### Entidades Principales

#### Flight (Vuelo)
- `id`: Identificador único
- `number`: Número de vuelo
- `departure`: Aeropuerto de salida
- `arrival`: Aeropuerto de llegada
- `departureTime`: Fecha y hora de salida
- `status`: Estado (SCHEDULED, CANCELLED)
- `capacity`: Capacidad total
- `occupancy`: Ocupación actual

#### Airport (Aeropuerto)
- `acronym`: Código del aeropuerto (ej: BCN, MAD)
- `name`: Nombre completo
- `country`: País
- `latitude`, `longitude`: Coordenadas

#### User (Usuario)
- `id`: ID único (TSID)
- `email`: Email único
- `password`: Contraseña encriptada (BCrypt)
- `roles`: Roles asignados (USER, ADMIN)

#### FlightBooking (Reserva)
- `locator`: Código de reserva (UUID)
- `user`: Usuario que realiza la reserva
- `flight`: Vuelo reservado

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.2.0**
- **Spring Security** con autenticación basada en formularios y JWT
- **Spring Data JPA** con Hibernate
- **MariaDB/H2** para base de datos
- **Thymeleaf** para plantillas HTML
- **ModelMapper** para conversión de DTOs
- **Lombok** para reducir boilerplate
- **JavaFaker** para datos de prueba

### Frontend
- **Bootstrap 5.3.7** para estilos
- **Thymeleaf** como motor de plantillas
- **WebJars** para gestión de dependencias frontend

### Testing
- **JUnit 5**
- **Mockito**
- **AssertJ**
- **Spring Boot Test**
- **H2** para tests de integración

## 📝 Endpoints REST Principales

### Flight Web API (Interno)
```
GET    /flight/flights           - Listar vuelos
GET    /flight/flights-edit/{id} - Formulario edición
POST   /flight/flights-edit      - Guardar vuelo
GET    /flight/resources/{id}    - Obtener imagen
GET    /flight/bookings          - Ver reservas
POST   /flight/bookings-confirm  - Confirmar reserva
```

### File Store API REST
```
GET    /store/api/resources/{id}  - Obtener recurso
POST   /store/api/resources       - Subir recurso
DELETE /store/api/resources/{id}  - Eliminar recurso
```

## 🔒 Seguridad

### Flight Web
- Autenticación basada en sesiones
- CSRF protection habilitado
- Roles: `ROLE_USER`, `ROLE_ADMIN`
- Passwords encriptados con BCrypt

### File Store API
- Autenticación JWT
- Tokens con duración de 1 hora
- Autorización basada en authorities

## 🧪 Testing

Ejecutar todos los tests:
```bash
mvn test
```

Tests incluidos:
- Tests unitarios de servicios
- Tests de integración con base de datos
- Tests de seguridad
- Tests de repositorios JPA

## 📦 Estructura del Proyecto

```
flight-app/
├── pom.xml                    # POM padre
├── docker-compose-dev.yml     # Configuración Docker
│
├── flight-web/
│   ├── src/main/java/
│   │   └── com/tokioschool/flightapp/
│   │       ├── flight/       # Dominio principal
│   │       │   ├── domain/   # Entidades JPA
│   │       │   ├── controller/# Controladores MVC
│   │       │   ├── service/  # Servicios
│   │       │   ├── repository/# Repositorios
│   │       │   └── security/ # Configuración seguridad
│   │       ├── email/        # Servicio de emails
│   │       └── store/        # Cliente para file-store-api
│   │
│   └── src/main/resources/
│       ├── templates/        # Plantillas Thymeleaf
│       ├── static/          # Recursos estáticos
│       ├── messages/        # Archivos i18n
│       └── scripts/database/ # Scripts SQL
│
└── file-store-api/
    └── src/main/java/
        └── com/tokioschool/flightapp/
            ├── base/         # API ejemplo (libros)
            ├── store/        # API almacenamiento
            └── core/         # Configuración común
```

## 🚦 Estado del Proyecto

- ✅ Gestión completa de vuelos
- ✅ Sistema de reservas
- ✅ Autenticación y autorización
- ✅ Almacenamiento de archivos
- ✅ Internacionalización
- ✅ Tests unitarios e integración
- ⏳ Gestión de pagos (pendiente)
- ⏳ API REST completa para flight-web (pendiente)

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para TokioSchool.
