# User Service

A Spring Boot microservice responsible for user management and authentication in the Cabinet Medical application.

## 📋 Overview

This service handles:
- User registration and authentication
- JWT token generation and validation
- User CRUD operations
- Role-based access control

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 4.0.0 |
| Spring Security | - |
| Spring Data JPA | - |
| Spring Cloud (Eureka Client) | 2025.1.0 |
| Spring Cloud OpenFeign | - |
| Apache Kafka | - |
| PostgreSQL | - |
| JWT (jjwt) | 0.11.5 |
| Lombok | - |

## 🏗️ Architecture

```
com.example.user_service
├── config/                    # Security and Kafka configurations
│   ├── CustomUserDetails.java
│   ├── JwtAuthFilter.java
│   ├── KafkaConsumerConfig.java
│   └── SecurityConfig.java
├── controller/                # REST API endpoints
│   ├── AuthController.java
│   └── UserController.java
├── entity/                    # JPA entities
│   ├── User.java
│   └── UserRole.java
├── exception/                 # Custom exceptions
│   └── AppException.java
├── mapper/                    # DTO mappers
│   ├── EntityToRes.java
│   └── ReqToEntity.java
├── notificationEvent/         # Kafka notification events
│   ├── NotificationListener.java
│   ├── NotificationMessage.java
│   └── NotificationType.java
├── repository/                # Data access layer
│   └── UserRepo.java
├── request/                   # Request DTOs
│   └── UserRequest.java
├── response/                  # Response DTOs
│   └── AuthResponse.java
├── service/                   # Business logic
│   ├── jwtServices/
│   │   ├── JwtUtils.java
│   │   ├── TokenBlacklistService.java
│   │   └── UserDetailsServiceImpl.java
│   ├── loginServices/
│   │   └── LoginServices.java
│   ├── logoutServices/
│   │   └── LogoutServices.java
│   └── userServices/
│       ├── UserService.java
│       └── UserServiceImpl.java
└── UserServiceApplication.java
```

## 📦 Entity

### User

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Primary key (auto-generated) |
| `login` | String | Unique username |
| `pwd` | String | Password (encrypted) |
| `nom` | String | Last name |
| `prenom` | String | First name |
| `signature` | String | Unique image path to user signature |
| `numTel` | String | Unique phone number |
| `role` | UserRole | User role (enum) |

### UserRole (Enum)

| Value | Description |
|-------|-------------|
| `MEDECIN` | Medical doctor |
| `ADMIN` | Administrator |
| `SECRETARY` | Secretary |

## 📨 DTOs

### Request DTOs

#### UserRequest

Used for user creation, update, and login operations.

```java
{
    "id": Long,           // Optional (for updates)
    "login": String,
    "pwd": String,
    "nom": String,
    "prenom": String,
    "signature": String,
    "numTel": String,
    "role": UserRole      // MEDECIN, ADMIN, or SECRETARY
}
```

### Response DTOs

#### UserResponse

Returned from user operations (from contracts library).

```java
{
    "id": Long,
    "login": String,
    "nom": String,
    "prenom": String,
    "numTel": String,
    "signature": String,
    "role": String
}
```

#### AuthResponse

Returned from token validation endpoint.

```java
{
    "token": String,
    "isTokenExpired": boolean,
    "error": String,
    "userRole": String
}
```

## 🔌 API Endpoints

### Authentication Controller (`/api/auth`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/login` | Authenticate user | `UserRequest` | JWT Token (String) |
| `POST` | `/register` | Register new user | `UserRequest` | `UserResponse` |
| `GET` | `/validate-token` | Validate JWT token | Query param: `token` | `AuthResponse` |

### User Controller (`/api/users`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/` | Create new user | `UserRequest` | `UserResponse` |
| `GET` | `/` | Get all users | - | `List<UserResponse>` |
| `GET` | `/{id}` | Get user by ID | - | `UserResponse` |
| `GET` | `/byLogin/{login}` | Get user by login | - | `UserResponse` |
| `PUT` | `/` | Update user | `UserRequest` | `UserResponse` |
| `DELETE` | `/{id}` | Delete user by ID | - | `204 No Content` |

## 🔐 Security

### JWT Configuration

The service uses JWT (JSON Web Tokens) for stateless authentication:

- **Token Expiration**: 24 hours (86400000 ms)
- **Algorithm**: HS256
- **Token Claims**:
  - `sub` (subject): Username
  - `roles`: User roles
  - `id`: User ID
  - `iat`: Issued at timestamp
  - `exp`: Expiration timestamp

### Public Endpoints

The following endpoints are publicly accessible without authentication:

- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/auth/validate-token`
- `/api/users/**`

All other endpoints require a valid JWT token.

## 📡 Kafka Integration

The service integrates with Apache Kafka for event-driven notifications.

### Notification Events

#### NotificationType (Enum)

| Value | Description |
|-------|-------------|
| `INFO` | Informational notification |
| `ALERT` | Alert notification |

#### NotificationMessage

```java
{
    "id": Long,
    "userId": Long,
    "message": String,
    "notificationType": NotificationType,
    "createdAt": LocalDateTime
}
```

## ☁️ Service Discovery

This service is registered with **Netflix Eureka** for service discovery in a microservices architecture.

## ⚙️ Configuration

### Application Properties

| Property | Value | Description |
|----------|-------|-------------|
| `server.port` | 8081 | Service port |
| `spring.application.name` | user-service | Service name |
| `spring.datasource.url` | jdbc:postgresql://localhost:5432/cabinet-medical | Database URL |
| `spring.kafka.bootstrap-servers` | localhost:9092 | Kafka broker |
| `jwt.expirationMs` | 86400000 | JWT token expiration (24h) |

## 🚀 Getting Started

### Prerequisites

- Java 21
- PostgreSQL
- Apache Kafka
- Maven

### Running the Service

```bash
# Clone the repository
git clone <repository-url>

# Navigate to the service directory
cd user-service

# Build the project
./mvnw clean install

# Run the service
./mvnw spring-boot:run
```

### Database Setup

Ensure PostgreSQL is running and create the database:

```sql
CREATE DATABASE "cabinet-medical";
```

The service uses `hibernate.ddl-auto=update`, so tables will be created automatically.

## 📚 Dependencies

This service depends on:

- **contracts** (`com.example:contracts:1.0-SNAPSHOT`): Shared DTOs library containing `UserResponse` and other shared models

## 🔗 Related Services

This user-service is part of the Cabinet Medical microservices architecture and communicates with other services via:

- **Eureka Server**: Service registration and discovery
- **Kafka**: Event-driven communication for notifications
- **OpenFeign**: Inter-service REST communication

