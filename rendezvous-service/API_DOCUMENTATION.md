# 📅 Rendezvous Service - API Documentation

## 📋 Table of Contents
- [Overview](#overview)
- [Configuration](#configuration)
- [Authentication & Authorization](#authentication--authorization)
- [Request Flow](#request-flow)
- [API Endpoints](#api-endpoints)
- [DTOs (Data Transfer Objects)](#dtos-data-transfer-objects)
- [Enums](#enums)
- [Frontend Integration Guide](#frontend-integration-guide)
- [Error Handling](#error-handling)

---

## 🔎 Overview

| Property | Value |
|----------|-------|
| **Service Name** | `rendezvous-service` |
| **Port** | `8083` |
| **Base URL** | `http://localhost:8083` |
| **Database** | PostgreSQL |
| **Framework** | Spring Boot 4.0.0 |
| **Java Version** | 21 |

### Key Features
- Appointment (Rendez-vous) management
- Waiting list (Liste d'attente) management
- JWT-based authentication
- Role-based access control (RBAC)
- Eureka service discovery integration
- Feign client for inter-service communication

---

## ⚙️ Configuration

### `application.properties`
```properties
spring.application.name=rendezvous-service
server.port=8083

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/cabinet-medical
spring.datasource.username=postgres
spring.datasource.password=ana123

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expirationMs=86400000
```

### Dependencies (Key)
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-security` - Security
- `spring-boot-starter-validation` - Request validation
- `spring-cloud-starter-netflix-eureka-client` - Service discovery
- `spring-cloud-starter-openfeign` - HTTP client for microservices
- `jjwt-api` (0.11.5) - JWT handling

---

## 🔐 Authentication & Authorization

### How JWT Token is Processed

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        REQUEST FLOW                                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  1. Frontend sends request with header:                                  │
│     Authorization: Bearer <JWT_TOKEN>                                    │
│                                                                          │
│  2. JwtAuthFilter (OncePerRequestFilter)                                 │
│     ├── Extracts token from "Authorization" header                       │
│     ├── Validates token using JwtUtils.isTokenValid()                    │
│     ├── Extracts username from token (subject claim)                     │
│     ├── Extracts roles from "roles" claim                                │
│     ├── Creates GrantedAuthority with "ROLE_" prefix                     │
│     └── Sets SecurityContext with authentication                         │
│                                                                          │
│  3. SecurityConfig (SecurityFilterChain)                                 │
│     ├── CSRF disabled (stateless)                                        │
│     ├── Sessions: STATELESS                                              │
│     ├── Public endpoints: /v3/api-docs/**, /swagger-ui/**,               │
│     │                     /swagger-ui.html, /actuator/health             │
│     └── All other requests: AUTHENTICATED                                │
│                                                                          │
│  4. RoleAuthorizationInterceptor (HandlerInterceptor)                    │
│     ├── Checks @RequireRole annotation on controller methods             │
│     ├── Validates token via Feign call to user-service                   │
│     ├── Verifies user role against required roles                        │
│     └── Returns 401/403 if unauthorized                                  │
│                                                                          │
│  5. Controller method executes if authorized                             │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### Security Components

| Component | Description |
|-----------|-------------|
| `JwtAuthFilter` | Extracts JWT from header, validates, and sets Spring Security context |
| `JwtUtils` | Utility class for JWT operations (extract claims, validate token) |
| `SecurityConfig` | Configures HTTP security, session management, and filter chain |
| `RoleAuthorizationInterceptor` | Custom interceptor for role-based access using `@RequireRole` |
| `WebConfig` | Registers the role interceptor for `/api/rendezvous/**` paths |
| `FeignClientConfig` | Propagates JWT token to inter-service Feign calls |

### User Roles
| Role | Description |
|------|-------------|
| `ADMIN` | Full access to all operations |
| `MEDECIN` | Doctor - can view and manage consultations |
| `SECRETARY` | Secretary - manages appointments and waiting list |

---

## 📡 API Endpoints

### 🗓️ Rendez-Vous (Appointments) - Base URL: `/api/rendezvous`

| # | Method | Endpoint | Description | Roles Allowed |
|---|--------|----------|-------------|---------------|
| 1 | `POST` | `/api/rendezvous` | Create new appointment | SECRETARY, ADMIN |
| 2 | `GET` | `/api/rendezvous/all` | Get all appointments | MEDECIN, SECRETARY, ADMIN |
| 3 | `GET` | `/api/rendezvous/{id}` | Get appointment by ID | MEDECIN, SECRETARY, ADMIN |
| 4 | `PUT` | `/api/rendezvous/{id}` | Update appointment | SECRETARY, ADMIN |
| 5 | `DELETE` | `/api/rendezvous/{id}` | Delete appointment | ADMIN |
| 6 | `GET` | `/api/rendezvous/by-date?date=YYYY-MM-DD` | Get appointments by date | MEDECIN, SECRETARY, ADMIN |
| 7 | `GET` | `/api/rendezvous/by-medecin-and-date?medecinId={id}&date=YYYY-MM-DD` | Get appointments by doctor and date | MEDECIN, SECRETARY, ADMIN |
| 8 | `PATCH` | `/api/rendezvous/{id}/confirmer` | Confirm appointment | SECRETARY, ADMIN |
| 9 | `PATCH` | `/api/rendezvous/{id}/annuler` | Cancel appointment | MEDECIN, SECRETARY, ADMIN |
| 10 | `GET` | `/api/rendezvous/by-patient?patientId={id}` | Get appointments by patient | MEDECIN, SECRETARY, ADMIN |
| 11 | `GET` | `/api/rendezvous/cabinet/{cabinetId}` | Get all appointments for a cabinet | SECRETARY, ADMIN |
| 12 | `GET` | `/api/rendezvous/cabinet/{cabinetId}/medecin/{medecinId}?date=YYYY-MM-DD` | Get appointments by cabinet, doctor, and date | MEDECIN, SECRETARY, ADMIN |
| 13 | `GET` | `/api/rendezvous/cabinet/{cabinetId}/patient/{patientId}` | Get patient history in cabinet | MEDECIN, SECRETARY, ADMIN |

---

### 🕐 Liste d'Attente (Waiting List) - Base URL: `/api/liste-attente`

| # | Method | Endpoint | Description | Roles Allowed |
|---|--------|----------|-------------|---------------|
| 1 | `POST` | `/api/liste-attente/{rendezVousId}` | Add patient to waiting list (when they arrive) | All authenticated |
| 2 | `GET` | `/api/liste-attente/medecin/{medecinId}/today?cabinetId={id}` | Get today's waiting list for a doctor | All authenticated |
| 3 | `PATCH` | `/api/liste-attente/{id}/envoyer-au-medecin` | Send patient to doctor (change status) | All authenticated |

> ⚠️ **Note:** The Liste d'Attente controller does not have `@RequireRole` annotations, so any authenticated user can access these endpoints.

---

## 📖 Detailed API Reference

### 🗓️ RENDEZ-VOUS ENDPOINTS

---

#### 1. Create Appointment
```http
POST /api/rendezvous
```
**Roles:** `SECRETARY`, `ADMIN`

**Request Body:**
```json
{
  "dateRdv": "2026-01-15",
  "heureRdv": "10:30:00",
  "motif": "Consultation générale",
  "statut": "EN_ATTENTE",
  "notes": "Premier rendez-vous",
  "medecinId": 1,
  "patientId": 5,
  "cabinetId": 1
}
```

**Response:** `200 OK`
```json
{
  "idRendezVous": 1,
  "dateRdv": "2026-01-15",
  "heureRdv": "10:30:00",
  "motif": "Consultation générale",
  "statut": "EN_ATTENTE",
  "notes": "Premier rendez-vous",
  "medecinId": 1,
  "patientId": 5,
  "cabinetId": 1
}
```

---

#### 2. Get All Appointments
```http
GET /api/rendezvous/all
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 3. Get Appointment by ID
```http
GET /api/rendezvous/{id}
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Appointment ID |

**Response:** `200 OK` - `RendezVousResponse`

---

#### 4. Update Appointment
```http
PUT /api/rendezvous/{id}
```
**Roles:** `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Appointment ID |

**Request Body:** Same as Create

**Response:** `200 OK` - `RendezVousResponse`

---

#### 5. Delete Appointment
```http
DELETE /api/rendezvous/{id}
```
**Roles:** `ADMIN` only

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Appointment ID |

**Response:** `204 No Content`

---

#### 6. Get Appointments by Date
```http
GET /api/rendezvous/by-date?date=2026-01-15
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Query Parameters:**
| Parameter | Type | Format | Description |
|-----------|------|--------|-------------|
| `date` | LocalDate | `YYYY-MM-DD` | Date to filter |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 7. Get Appointments by Doctor and Date
```http
GET /api/rendezvous/by-medecin-and-date?medecinId=1&date=2026-01-15
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Query Parameters:**
| Parameter | Type | Format | Description |
|-----------|------|--------|-------------|
| `medecinId` | Long | - | Doctor ID |
| `date` | LocalDate | `YYYY-MM-DD` | Date to filter |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 8. Confirm Appointment
```http
PATCH /api/rendezvous/{id}/confirmer
```
**Roles:** `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Appointment ID |

**Response:** `200 OK` - `RendezVousResponse` with `statut: "CONFIRME"`

---

#### 9. Cancel Appointment
```http
PATCH /api/rendezvous/{id}/annuler
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Appointment ID |

**Response:** `200 OK` - `RendezVousResponse` with `statut: "ANNULE"`

---

#### 10. Get Appointments by Patient
```http
GET /api/rendezvous/by-patient?patientId=5
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `patientId` | Long | Patient ID |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 11. Get All Appointments by Cabinet
```http
GET /api/rendezvous/cabinet/{cabinetId}
```
**Roles:** `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `cabinetId` | Long | Cabinet ID |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 12. Get Appointments by Cabinet, Doctor, and Date
```http
GET /api/rendezvous/cabinet/{cabinetId}/medecin/{medecinId}?date=2026-01-15
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `cabinetId` | Long | Cabinet ID |
| `medecinId` | Long | Doctor ID |

**Query Parameters:**
| Parameter | Type | Format | Description |
|-----------|------|--------|-------------|
| `date` | String | `YYYY-MM-DD` | Date to filter |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

#### 13. Get Patient History in Cabinet
```http
GET /api/rendezvous/cabinet/{cabinetId}/patient/{patientId}
```
**Roles:** `MEDECIN`, `SECRETARY`, `ADMIN`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `cabinetId` | Long | Cabinet ID |
| `patientId` | Long | Patient ID |

**Response:** `200 OK` - Array of `RendezVousResponse`

---

### 🕐 LISTE D'ATTENTE ENDPOINTS

---

#### 1. Add Patient to Waiting List
```http
POST /api/liste-attente/{rendezVousId}
```
**Roles:** All authenticated users

**Description:** Called when a patient physically arrives at the cabinet

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `rendezVousId` | Long | The appointment ID |

**Response:** `200 OK`
```json
{
  "id": 1,
  "rendezVousId": 123,
  "medecinId": 1,
  "dateRdv": "2026-01-15",
  "position": 3,
  "statutAttente": "EN_ATTENTE",
  "heureArrivee": "2026-01-15T09:45:00",
  "cabinetId": 1
}
```

---

#### 2. Get Today's Waiting List for Doctor
```http
GET /api/liste-attente/medecin/{medecinId}/today?cabinetId=1
```
**Roles:** All authenticated users

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `medecinId` | Long | Doctor ID |

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `cabinetId` | Long | Cabinet ID |

**Response:** `200 OK` - Array of `ListeAttenteResponse`

---

#### 3. Send Patient to Doctor
```http
PATCH /api/liste-attente/{id}/envoyer-au-medecin
```
**Roles:** All authenticated users

**Description:** Changes the waiting status to indicate patient is now with the doctor

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | Long | Waiting list entry ID |

**Response:** `200 OK` - `ListeAttenteResponse` with `statutAttente: "EN_COURS"`

---

## 📦 DTOs (Data Transfer Objects)

### Request DTOs

#### `RendezVousRequest`
```typescript
interface RendezVousRequest {
  dateRdv: string;        // Required - Format: "YYYY-MM-DD"
  heureRdv: string;       // Required - Format: "HH:mm:ss"
  motif: string;          // Required - Reason for appointment
  statut: StatutRDV;      // Required - Appointment status
  notes?: string;         // Optional - Additional notes
  medecinId?: number;     // Optional - Doctor ID
  patientId?: number;     // Optional - Patient ID
  cabinetId?: number;     // Optional - Cabinet ID
}
```

### Response DTOs

#### `RendezVousResponse`
```typescript
interface RendezVousResponse {
  idRendezVous: number;
  dateRdv: string;        // Format: "YYYY-MM-DD"
  heureRdv: string;       // Format: "HH:mm:ss"
  motif: string;
  statut: StatutRDV;
  notes: string | null;
  medecinId: number;
  patientId: number;
  cabinetId: number;
}
```

#### `ListeAttenteResponse`
```typescript
interface ListeAttenteResponse {
  id: number;
  rendezVousId: number;
  medecinId: number;
  dateRdv: string;             // Format: "YYYY-MM-DD"
  position: number;            // Position in queue
  statutAttente: StatutAttente;
  heureArrivee: string;        // Format: "YYYY-MM-DDTHH:mm:ss"
  cabinetId: number;
}
```

#### `AuthResponse` (from user-service)
```typescript
interface AuthResponse {
  token: string;
  tokenExpired: boolean;
  error: string | null;
  userRole: string;
}
```

#### `UserResponse` (from user-service)
```typescript
interface UserResponse {
  id: number;
  login: string;
  pwd: string;
  nom: string;
  prenom: string;
  signature: string;     // Path to signature image
  numTel: string;
  role: string;
}
```

---

## 📊 Enums

### `StatutRDV` (Appointment Status)
```typescript
enum StatutRDV {
  EN_ATTENTE = "EN_ATTENTE",    // Pending
  CONFIRME = "CONFIRME",         // Confirmed
  ANNULE = "ANNULE",             // Cancelled
  EN_COURS = "EN_COURS",         // In progress (patient in cabinet)
  TERMINE = "TERMINE"            // Completed
}
```

### `StatutAttente` (Waiting Status)
```typescript
enum StatutAttente {
  EN_ATTENTE = "EN_ATTENTE",     // Patient in waiting room
  EN_COURS = "EN_COURS",         // Currently in consultation
  TERMINE = "TERMINE",           // Consultation finished
  ABSENT = "ABSENT"              // Patient didn't show up / left
}
```

---



## ❌ Error Handling

### HTTP Status Codes

| Code | Description | When |
|------|-------------|------|
| `200` | OK | Successful GET, PUT, PATCH |
| `204` | No Content | Successful DELETE |
| `400` | Bad Request | Validation errors |
| `401` | Unauthorized | Missing/invalid/expired token |
| `403` | Forbidden | Insufficient role permissions |
| `404` | Not Found | Resource doesn't exist |
| `500` | Internal Server Error | Server error |

### Error Response Format

```json
{
  "timestamp": "2026-01-09T10:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Rôle non autorisé: PATIENT. Rôles requis: SECRETARY, ADMIN",
  "path": "/api/rendezvous"
}
```

---

## 🔌 Inter-Service Communication

### Feign Client: `UserFeignClient`

This service communicates with `user-service` via Feign:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/users/{id}` | Get user details by ID |
| `GET` | `/api/auth/validate-token?token=X` | Validate JWT token |

> The `FeignClientConfig` automatically propagates the `Authorization` header to inter-service calls.

---

## 📁 Project Structure

```
src/main/java/ma/cabinet/rendezvous_service/
├── RendezvousServiceApplication.java
├── config/
│   ├── FeignClientConfig.java      # Feign JWT propagation
│   ├── SecurityConfig.java         # Spring Security config
│   └── WebConfig.java              # Interceptor registration
├── controller/
│   ├── RendezVousController.java   # Appointment endpoints
│   └── ListeAttenteController.java # Waiting list endpoints
├── entity/
│   ├── RendezVous.java             # Appointment entity
│   └── ListeAttente.java           # Waiting list entity
├── enums/
│   ├── StatutRDV.java              # Appointment status enum
│   └── StatutAttente.java          # Waiting status enum
├── feign/
│   └── UserFeignClient.java        # User service client
├── mapper/
│   ├── EntityToRequest.java
│   └── EntityToResponse.java
├── repository/
│   ├── RendezVousRepository.java
│   └── ListeAttenteRepository.java
├── request/
│   └── RendezVousRequest.java      # Request DTO
├── response/
│   ├── RendezVousResponse.java     # Response DTO
│   ├── ListeAttenteResponse.java
│   ├── AuthResponse.java
│   └── UserResponse.java
├── security/
│   ├── JwtAuthFilter.java          # JWT extraction filter
│   ├── JwtUtils.java               # JWT utilities
│   └── RoleAuthorizationInterceptor.java  # Role checker
└── service/
    ├── RendezVousService.java
    ├── RendezVousServiceImpl.java
    ├── ListeAttenteService.java
    ├── ListeAttenteServiceImpl.java
    └── RdvValidations.java         # Validation utilities
```

---

## ✅ Quick Reference Card

### Creating an Appointment
```http
POST /api/rendezvous
Authorization: Bearer <token>
Content-Type: application/json

{
  "dateRdv": "2026-01-15",
  "heureRdv": "10:30:00",
  "motif": "Consultation générale",
  "statut": "EN_ATTENTE",
  "notes": "Premier rendez-vous",
  "medecinId": 1,
  "patientId": 5,
  "cabinetId": 1
}
```

### Get Appointments for a Doctor Today
```http
GET /api/rendezvous/by-medecin-and-date?medecinId=1&date=2026-01-09
Authorization: Bearer <token>
```

### Add Patient to Waiting List
```http
POST /api/liste-attente/123
Authorization: Bearer <token>
```

---

**Last Updated:** January 9, 2026

