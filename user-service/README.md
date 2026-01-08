# User Service - API Documentation

A Spring Boot microservice for user management and authentication in the Cabinet Medical application.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication Flow](#authentication-flow)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication-endpoints)
  - [User Management](#user-management-endpoints)
- [Data Models](#data-models)
- [Error Handling](#error-handling)
- [Token Management](#token-management)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start-for-backend)

---

## 🎯 Overview

This service handles:
- ✅ User registration and login
- ✅ JWT token generation and validation
- ✅ User CRUD operations
- ✅ Role-based access control (MEDECIN, ADMIN, SECRETARY)
- ✅ Token logout/invalidation

---

## 🌐 Base URL

```
http://localhost:8081
```

---

## 🔐 Authentication Flow

### Login Flow

```
┌─────────┐          ┌──────────────┐          ┌──────────┐
│ Frontend│          │ User Service │          │ Database │
└────┬────┘          └──────┬───────┘          └────┬─────┘
     │                      │                       │
     │  POST /api/auth/login│                       │
     │  {login, pwd}        │                       │
     │─────────────────────>│                       │
     │                      │  Validate credentials │
     │                      │──────────────────────>│
     │                      │                       │
     │                      │<──────────────────────│
     │    JWT Token         │                       │
     │<─────────────────────│                       │
     │                      │                       │
     │  Store token locally │                       │
     │                      │                       │
```

### Protected Request Flow

```
┌─────────┐                      ┌──────────────┐
│ Frontend│                      │ User Service │
└────┬────┘                      └──────┬───────┘
     │                                  │
     │  GET /api/users                  │
     │  Header: Authorization: Bearer <token>
     │─────────────────────────────────>│
     │                                  │
     │  ✅ Valid token → Data           │
     │  ❌ Invalid/Expired → 401        │
     │<─────────────────────────────────│
     │                                  │
```

---

## 📡 API Endpoints

### Authentication Endpoints

#### 1. Login

Authenticate a user and receive a JWT token.

| Property | Value |
|----------|-------|
| **URL** | `/api/auth/login` |
| **Method** | `POST` |
| **Auth Required** | ❌ No |
| **Content-Type** | `application/json` |

**Request Body:**
```json
{
  "login": "user@example.com",
  "pwd": "password123"
}
```

**Success Response (200 OK):**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZXMiOiJNRURFQ0lOIiwiaWQiOjEsImlhdCI6MTcwNDEwMjQwMCwiZXhwIjoxNzA0MTg4ODAwfQ.xxx
```

**Error Response (401 Unauthorized):**
```json
{
  "message": "Login ou mot de passe incorrect",
  "status": "UNAUTHORIZED",
  "code": "ERR_INVALID_CREDENTIALS"
}
```

---

#### 2. Register

Create a new user account.

| Property | Value |
|----------|-------|
| **URL** | `/api/auth/register` |
| **Method** | `POST` |
| **Auth Required** | ❌ No |
| **Content-Type** | `application/json` |

**Request Body:**
```json
{
  "cabinetId": 1,
  "login": "newuser@example.com",
  "pwd": "password123",
  "nom": "Dupont",
  "prenom": "Jean",
  "signature": "/signatures/jean_dupont.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "cabinetId": 1,
  "nomCabinet": null,
  "login": "newuser@example.com",
  "nom": "Dupont",
  "prenom": "Jean",
  "signature": "/signatures/jean_dupont.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

**Error Response (409 Conflict):**
```json
{
  "message": "Utilisateur déjà existant",
  "status": "CONFLICT",
  "code": "ERR_EMAIL_EXISTS"
}
```

---

#### 3. Validate Token

Check if a JWT token is still valid.

| Property | Value |
|----------|-------|
| **URL** | `/api/auth/validate-token` |
| **Method** | `GET` |
| **Auth Required** | ❌ No |
| **Query Params** | `token` |

**Request:**
```
GET /api/auth/validate-token?token=eyJhbGciOiJIUzI1NiIs...
```

**Success Response - Valid Token (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "isTokenExpired": false,
  "error": null,
  "userRole": "MEDECIN"
}
```

**Response - Expired Token (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "isTokenExpired": true,
  "error": "Token expiré",
  "userRole": null
}
```

---

#### 4. Logout

Invalidate a JWT token (adds to blacklist).

| Property | Value |
|----------|-------|
| **URL** | `/api/auth/logout` |
| **Method** | `POST` |
| **Auth Required** | ✅ Yes (Bearer Token) |

**Request Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

**Success Response (200 OK):**
```
(empty body)
```

**Error Response (400 Bad Request):**
```
(missing or malformed Authorization header)
```

---

### User Management Endpoints

> ⚠️ **Note:** These endpoints are currently public (`/api/users/**`), but they should be protected in production.

#### 1. Get All Users

| Property | Value |
|----------|-------|
| **URL** | `/api/users` |
| **Method** | `GET` |
| **Auth Required** | ❌ No (currently public) |

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "cabinetId": 1,
    "nomCabinet": "Cabinet Santé Plus",
    "login": "doctor@example.com",
    "nom": "Martin",
    "prenom": "Pierre",
    "signature": "/signatures/pierre_martin.png",
    "numTel": "+212612345678",
    "role": "MEDECIN"
  },
  {
    "id": 2,
    "cabinetId": 1,
    "nomCabinet": "Cabinet Santé Plus",
    "login": "admin@example.com",
    "nom": "Dubois",
    "prenom": "Marie",
    "signature": null,
    "numTel": "+212698765432",
    "role": "ADMIN"
  }
]
```

---

#### 2. Get User by ID

| Property | Value |
|----------|-------|
| **URL** | `/api/users/{id}` |
| **Method** | `GET` |
| **Auth Required** | ❌ No (currently public) |

**Success Response (200 OK):**
```json
{
  "id": 1,
  "cabinetId": 1,
  "nomCabinet": "Cabinet Santé Plus",
  "login": "doctor@example.com",
  "nom": "Martin",
  "prenom": "Pierre",
  "signature": "/signatures/pierre_martin.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

---

#### 3. Get User by Login

| Property | Value |
|----------|-------|
| **URL** | `/api/users/byLogin/{login}` |
| **Method** | `GET` |
| **Auth Required** | ❌ No (currently public) |

**Success Response (200 OK):**
```json
{
  "id": 1,
  "cabinetId": 1,
  "nomCabinet": "Cabinet Santé Plus",
  "login": "doctor@example.com",
  "nom": "Martin",
  "prenom": "Pierre",
  "signature": "/signatures/pierre_martin.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

---

#### 4. Create User

| Property | Value |
|----------|-------|
| **URL** | `/api/users` |
| **Method** | `POST` |
| **Auth Required** | ❌ No (currently public) |
| **Content-Type** | `application/json` |

**Request Body:**
```json
{
  "cabinetId": 1,
  "login": "newdoctor@example.com",
  "pwd": "securePassword123",
  "nom": "Leroy",
  "prenom": "Sophie",
  "signature": "/signatures/sophie_leroy.png",
  "numTel": "+212611223344",
  "role": "MEDECIN"
}
```

**Success Response (201 Created):**
```json
{
  "id": 3,
  "cabinetId": 1,
  "nomCabinet": null,
  "login": "newdoctor@example.com",
  "nom": "Leroy",
  "prenom": "Sophie",
  "signature": "/signatures/sophie_leroy.png",
  "numTel": "+212611223344",
  "role": "MEDECIN"
}
```

---

#### 5. Update User

| Property | Value |
|----------|-------|
| **URL** | `/api/users` |
| **Method** | `PUT` |
| **Auth Required** | ❌ No (currently public) |
| **Content-Type** | `application/json` |

**Request Body:**
```json
{
  "id": 1,
  "cabinetId": 1,
  "login": "doctor@example.com",
  "pwd": "newPassword123",
  "nom": "Martin",
  "prenom": "Pierre-Louis",
  "signature": "/signatures/pierre_martin_v2.png",
  "numTel": "+212612345679",
  "role": "MEDECIN"
}
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "cabinetId": 1,
  "nomCabinet": null,
  "login": "doctor@example.com",
  "nom": "Martin",
  "prenom": "Pierre-Louis",
  "signature": "/signatures/pierre_martin_v2.png",
  "numTel": "+212612345679",
  "role": "MEDECIN"
}
```

---

#### 6. Get Users by Cabinet ID

| Property | Value |
|----------|-------|
| **URL** | `/api/users/byCabinet/{cabinetId}` |
| **Method** | `GET` |
| **Auth Required** | ❌ No (currently public) |

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "cabinetId": 1,
    "nomCabinet": null,
    "login": "doctor@example.com",
    "nom": "Martin",
    "prenom": "Pierre",
    "signature": "/signatures/pierre_martin.png",
    "numTel": "+212612345678",
    "role": "MEDECIN"
  },
  {
    "id": 2,
    "cabinetId": 1,
    "nomCabinet": null,
    "login": "secretary@example.com",
    "nom": "Dubois",
    "prenom": "Marie",
    "signature": null,
    "numTel": "+212698765432",
    "role": "SECRETARY"
  }
]
```

---

#### 7. Delete User

| Property | Value |
|----------|-------|
| **URL** | `/api/users/{id}` |
| **Method** | `DELETE` |
| **Auth Required** | ❌ No (currently public) |

**Success Response (204 No Content):**
```
(empty body)
```

---

## 📦 Data Models

### User Roles

| Role | Description |
|------|-------------|
| `MEDECIN` | Medical doctor - can access patient records |
| `ADMIN` | Administrator - full system access |
| `SECRETARY` | Secretary - limited access for scheduling |

### Request DTOs

#### UserRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | Long | No (Yes for updates) | User ID |
| `cabinetId` | Long | Yes | Cabinet ID the user belongs to |
| `login` | String | Yes | Email or username |
| `pwd` | String | Yes | Password (plain text, will be encrypted) |
| `nom` | String | Yes | Last name |
| `prenom` | String | Yes | First name |
| `signature` | String | No | Path to signature image |
| `numTel` | String | Yes | Phone number |
| `role` | UserRole | Yes | MEDECIN, ADMIN, or SECRETARY |

**Example:**
```json
{
  "id": 1,
  "cabinetId": 1,
  "login": "user@example.com",
  "pwd": "password123",
  "nom": "Dupont",
  "prenom": "Jean",
  "signature": "/signatures/jean_dupont.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

### Response DTOs

#### UserResponse

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | User ID |
| `cabinetId` | Long | Cabinet ID the user belongs to |
| `nomCabinet` | String | Cabinet name (nullable, populated on getAllUsers) |
| `login` | String | Email or username |
| `nom` | String | Last name |
| `prenom` | String | First name |
| `signature` | String | Path to signature image (nullable) |
| `numTel` | String | Phone number |
| `role` | String | User role |

**Example:**
```json
{
  "id": 1,
  "cabinetId": 1,
  "nomCabinet": "Cabinet Santé Plus",
  "login": "user@example.com",
  "nom": "Dupont",
  "prenom": "Jean",
  "signature": "/signatures/jean_dupont.png",
  "numTel": "+212612345678",
  "role": "MEDECIN"
}
```

#### AuthResponse

| Field | Type | Description |
|-------|------|-------------|
| `token` | String | JWT token |
| `isTokenExpired` | boolean | Whether the token is expired |
| `error` | String | Error message (nullable) |
| `userRole` | String | User role from token (nullable) |

**Example:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "isTokenExpired": false,
  "error": null,
  "userRole": "MEDECIN"
}
```

---

## ⚠️ Error Handling

### Error Response Format

| Field | Type | Description |
|-------|------|-------------|
| `message` | String | Error message |
| `status` | String | HTTP status name |
| `code` | String | Error code |

**Example:**
```json
{
  "message": "Login ou mot de passe incorrect",
  "status": "UNAUTHORIZED",
  "code": "ERR_INVALID_CREDENTIALS"
}
```

### Common Error Codes

| HTTP Status | Code | Message | Description |
|-------------|------|---------|-------------|
| 401 | `ERR_INVALID_CREDENTIALS` | Login ou mot de passe incorrect | Wrong username or password |
| 401 | - | Token invalidé (blacklist) | Token has been logged out |
| 401 | - | Invalid JWT token | Malformed or tampered token |
| 404 | `ERR_USER_NOT_FOUND` | Utilisateur introuvable | User doesn't exist |
| 409 | `ERR_EMAIL_EXISTS` | Utilisateur déjà existant | Email already registered |

---

## 🔑 Token Management

### JWT Token Structure

The JWT token contains the following claims:

```json
{
  "sub": "user@example.com",    // Username (subject)
  "roles": "MEDECIN",           // User role
  "id": 1,                      // User ID
  "iat": 1704102400,            // Issued at (timestamp)
  "exp": 1704188800             // Expiration (timestamp)
}
```

### Token Expiration

- **Default expiration**: 24 hours (86400000 ms)
- When token expires, API returns 401 Unauthorized
- Frontend should redirect to login page

### Token Storage Recommendations

| Storage | Pros | Cons | Recommended For |
|---------|------|------|-----------------|
| `localStorage` | Persists across sessions | XSS vulnerable | Simple SPAs |
| `sessionStorage` | Auto-clears on tab close | XSS vulnerable | Sensitive apps |
| `HttpOnly Cookie` | XSS protected | CSRF vulnerable | High security |

---

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
├── repository/                # Data access layer
│   └── UserRepo.java
├── request/                   # Request DTOs
│   └── UserRequest.java
├── response/                  # Response DTOs
│   └── AuthResponse.java
├── service/                   # Business logic
│   ├── jwtServices/
│   │   ├── JwtUtils.java
│   │   └── TokenBlacklistService.java
│   ├── loginServices/
│   │   ├── LoginServices.java
│   │   └── LoginServicesImpl.java
│   ├── logoutServices/
│   │   ├── LogoutServices.java
│   │   └── LogoutServicesImpl.java
│   └── userServices/
│       ├── UserService.java
│       └── UserServiceImpl.java
└── UserServiceApplication.java
```

---

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 4.0.0 |
| Spring Security | - |
| Spring Data JPA | - |
| PostgreSQL | - |
| JWT (jjwt) | 0.11.5 |
| Lombok | - |

---

## 🚀 Quick Start for Backend

```bash
# Prerequisites: Java 21, PostgreSQL, Maven

# Create database
psql -U postgres -c "CREATE DATABASE \"cabinet-medical\";"

# Run the service
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8081`

---

## 📞 Support

For questions or issues, please contact the backend team.

