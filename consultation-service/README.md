# 🏥 Consultation Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Description

Le **Consultation Service** est un microservice faisant partie du projet de gestion d'un cabinet médical. Ce service est responsable de la gestion complète des consultations médicales, des ordonnances et des médicaments.

## 🏗️ Architecture

Ce projet suit une **architecture microservices** avec les caractéristiques suivantes :

```
consultation-service/
├── src/main/java/com/cabinet/consultationservice/
│   ├── config/                 # Configuration (OpenAPI, Exception Handler)
│   ├── controller/              # Contrôleurs REST (API endpoints)
│   ├── dto/                     # Data Transfer Objects (Request/Response)
│   ├── enums/                   # Enumerations
│   ├── exception/               # Exceptions personnalisées
│   ├── mapper/                  # Mappers Entity <-> DTO
│   ├── model/                   # Entités JPA
│   ├── repository/              # Repositories Spring Data JPA
│   └── service/                 # Services métier
│       └── impl/                # Implémentations des services
└── src/main/resources/
    └── application.yml          # Configuration de l'application
```

### 🔄 Pattern Utilisé

- **Layered Architecture** (Architecture en couches)
- **DTO Pattern** (Séparation Request/Response)
- **Repository Pattern** avec Spring Data JPA
- **Dependency Injection** via Spring

---

## 🗃️ Entités (Entities)

### 📌 Consultation

| Champ                  | Type        | Contraintes                    | Description                    |
|------------------------|-------------|--------------------------------|--------------------------------|
| `id`                   | Long        | PK, Auto-generated             | Identifiant unique             |
| `patientId`             | Long        | NOT NULL                       | ID du patient (référence)      |
| `rendezVousId`          | Long        | NOT NULL                       | ID du rendez-vous (référence)  |
| `medecinId`             | Long        | NOT NULL                       | ID du médecin                  |
| `type`                  | String(50)  | NOT NULL                       | CONSULTATION ou CONTROLE       |
| `dateConsultation`      | LocalDate   | NOT NULL                       | Date de la consultation        |
| `examenClinique`        | TEXT        | Nullable                       | Examen clinique                |
| `examenSupplementaire`  | TEXT        | Nullable                       | Examens supplémentaires        |
| `diagnostic`             | TEXT        | Nullable                       | Diagnostic                     |
| `observations`          | TEXT        | Nullable                       | Observations                   |
| `createdAt`             | LocalDateTime | NOT NULL, Auto-generated    | Date de création               |

**Relation :** `OneToMany` avec `Ordonnance` (Une consultation peut avoir plusieurs ordonnances)

### 📌 Ordonnance

| Champ           | Type              | Contraintes                    | Description                    |
|-----------------|-------------------|--------------------------------|--------------------------------|
| `id`            | Long              | PK, Auto-generated             | Identifiant unique             |
| `type`          | TypeOrdonnance    | NOT NULL                       | MEDICAMENT ou EXAMEN           |
| `contenuLibre`  | TEXT              | Nullable                       | Contenu libre de l'ordonnance  |
| `consultation`  | Consultation      | FK, NOT NULL                   | Consultation associée          |
| `createdAt`     | LocalDateTime     | NOT NULL, Auto-generated       | Date de création               |

**Relation :** 
- `ManyToOne` avec `Consultation` (Plusieurs ordonnances appartiennent à une consultation)
- `OneToMany` avec `Medicament` (Une ordonnance peut contenir plusieurs médicaments)

### 📌 Medicament

| Champ         | Type        | Contraintes                    | Description                    |
|---------------|-------------|--------------------------------|--------------------------------|
| `id`          | Long        | PK, Auto-generated             | Identifiant unique             |
| `nom`         | String(200) | NOT NULL                       | Nom du médicament              |
| `description` | TEXT        | Nullable                       | Description                    |
| `dosage`      | String(100) | Nullable                       | Dosage                         |
| `duree`       | String(100) | Nullable                       | Durée du traitement            |
| `actif`       | Boolean     | NOT NULL, Default: true        | Statut actif (soft delete)     |
| `ordonnance`  | Ordonnance  | FK, NOT NULL                   | Ordonnance associée            |

**Relation :** `ManyToOne` avec `Ordonnance` (Plusieurs médicaments appartiennent à une ordonnance)

### 📌 Enum: TypeOrdonnance

- `MEDICAMENT` - Prescription de médicament
- `EXAMEN` - Prescription d'examen

---

## 🌐 API Endpoints

### 🩺 Consultation Controller

Base URL: `/api/consultations`

| Méthode | Endpoint                        | Description                              | Request Body              | Response                    |
|---------|---------------------------------|------------------------------------------|---------------------------|-----------------------------|
| `POST`  | `/api/consultations`            | Créer une nouvelle consultation          | ConsultationRequestDTO    | ConsultationResponseDTO    |
| `GET`   | `/api/consultations/{id}`       | Récupérer une consultation par ID        | -                         | ConsultationResponseDTO     |
| `GET`   | `/api/consultations/patient/{patientId}` | Récupérer toutes les consultations d'un patient | -              | List<ConsultationResponseDTO> |
| `DELETE`| `/api/consultations/{id}`       | Supprimer une consultation               | -                         | 204 No Content              |

### 📋 Ordonnance Controller

Base URL: `/api/ordonnances`

| Méthode | Endpoint                                    | Description                              | Request Body           | Response                   |
|---------|---------------------------------------------|------------------------------------------|------------------------|----------------------------|
| `POST`  | `/api/ordonnances`                          | Créer une nouvelle ordonnance             | OrdonnanceRequestDTO   | OrdonnanceResponseDTO      |
| `GET`   | `/api/ordonnances/consultation/{consultationId}` | Récupérer toutes les ordonnances d'une consultation | -         | List<OrdonnanceResponseDTO> |

### 💊 Medicament Controller

Base URL: `/api/medicaments`

| Méthode | Endpoint                        | Description                              | Request Body           | Response                   |
|---------|---------------------------------|------------------------------------------|------------------------|----------------------------|
| `POST`  | `/api/medicaments`              | Ajouter un médicament à une ordonnance   | MedicamentRequestDTO   | MedicamentResponseDTO      |
| `GET`   | `/api/medicaments/search?nom=` | Rechercher des médicaments par nom       | -                      | List<MedicamentResponseDTO> |
| `PUT`   | `/api/medicaments/{id}/disable` | Désactiver un médicament (soft delete)   | -                      | 200 OK                     |

---

## ⚙️ Configuration

### Application Properties (`application.yml`)

```yaml
spring:
  application:
    name: consultation-service
  datasource:
    url: jdbc:mysql://localhost:3306/consultation_service_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: consultation_app
    password: consultation123
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8082

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### 📊 Base de données

- **Type:** MySQL 8.0
- **Database:** `consultation_service_db`
- **Port:** 3306

---

## 🚀 Démarrage

### Prérequis

- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Installation

1. **Cloner le repository**
   ```bash
   git clone <repository-url>
   cd consultation-service
   ```

2. **Configurer la base de données MySQL**
   ```sql
   CREATE DATABASE consultation_service_db;
   CREATE USER 'consultation_app'@'localhost' IDENTIFIED BY 'consultation123';
   GRANT ALL PRIVILEGES ON consultation_service_db.* TO 'consultation_app'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Compiler et lancer l'application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Accéder à l'application**
   - API: `http://localhost:8082/api/consultations`
   - Swagger UI: `http://localhost:8082/swagger-ui.html`
   - OpenAPI Docs: `http://localhost:8082/v3/api-docs`

---

## 📚 Documentation API (Swagger)

L'API est documentée avec **OpenAPI 3.0 / Swagger UI**.

Accéder à la documentation interactive :
```
http://localhost:8082/swagger-ui.html
```

### Exemples de requêtes

#### Créer une consultation
```json
POST /api/consultations
{
  "patientId": 1,
  "rendezVousId": 1,
  "medecinId": 1,
  "type": "CONSULTATION",
  "dateConsultation": "2024-01-15",
  "examenClinique": "Examen clinique normal",
  "diagnostic": "Grippe",
  "observations": "Repos recommandé"
}
```

#### Créer une ordonnance
```json
POST /api/ordonnances
{
  "consultationId": 1,
  "type": "MEDICAMENT",
  "contenuLibre": "Prendre 2 comprimés par jour pendant 7 jours"
}
```

#### Ajouter un médicament
```json
POST /api/medicaments
{
  "ordonnanceId": 1,
  "nom": "Paracétamol",
  "description": "Antalgique et antipyrétique",
  "dosage": "500mg",
  "duree": "7 jours"
}
```

---

## 🛠️ Technologies Utilisées

| Technologie               | Version | Description                          |
|--------------------------|---------|--------------------------------------|
| Spring Boot               | 3.3.4   | Framework principal                  |
| Spring Data JPA           | -       | Accès aux données                    |
| Spring Validation         | -       | Validation des DTOs                  |
| MySQL Connector           | -       | Driver MySQL                         |
| Lombok                    | -       | Réduction du boilerplate             |
| SpringDoc OpenAPI         | 2.6.0   | Documentation Swagger                |

---

## 🔧 Gestion des Exceptions

Le service utilise un `GlobalExceptionHandler` pour gérer les exceptions de manière centralisée :

| Exception                  | HTTP Status | Description                          |
|----------------------------|-------------|--------------------------------------|
| `ResourceNotFoundException`| 404         | Ressource non trouvée                |
| `ValidationException`      | 400         | Erreur de validation                 |
| `ConstraintViolationException` | 400    | Violation de contrainte              |
| `MethodArgumentNotValidException` | 400 | Erreur de validation des arguments |

---

## 📂 Structure des Packages

```
com.cabinet.consultationservice
├── ConsultationServiceApplication.java    # Point d'entrée
├── config
│   ├── GlobalExceptionHandler.java        # Gestion globale des exceptions
│   └── OpenAPIConfig.java                # Configuration Swagger
├── controller
│   ├── ConsultationController.java        # REST endpoints Consultation
│   ├── OrdonnanceController.java         # REST endpoints Ordonnance
│   └── MedicamentController.java         # REST endpoints Medicament
├── dto
│   ├── ConsultationRequestDTO.java        # DTO requête Consultation
│   ├── ConsultationResponseDTO.java       # DTO réponse Consultation
│   ├── OrdonnanceRequestDTO.java          # DTO requête Ordonnance
│   ├── OrdonnanceResponseDTO.java         # DTO réponse Ordonnance
│   ├── MedicamentRequestDTO.java          # DTO requête Medicament
│   └── MedicamentResponseDTO.java         # DTO réponse Medicament
├── enums
│   └── TypeOrdonnance.java                # Enum TypeOrdonnance
├── exception
│   ├── ApiError.java                      # Format erreur API
│   ├── ResourceNotFoundException.java
│   └── ValidationException.java
├── mapper
│   ├── ConsultationMapper.java            # Mapper Consultation
│   ├── OrdonnanceMapper.java             # Mapper Ordonnance
│   └── MedicamentMapper.java             # Mapper Medicament
├── model
│   ├── Consultation.java                  # Entité Consultation
│   ├── Ordonnance.java                   # Entité Ordonnance
│   └── Medicament.java                   # Entité Medicament
├── repository
│   ├── ConsultationRepository.java        # Repository Consultation
│   ├── OrdonnanceRepository.java         # Repository Ordonnance
│   └── MedicamentRepository.java          # Repository Medicament
└── service
    ├── ConsultationService.java          # Interface Service Consultation
    ├── OrdonnanceService.java            # Interface Service Ordonnance
    ├── MedicamentService.java            # Interface Service Medicament
    └── impl
        ├── ConsultationServiceImpl.java   # Implémentation Consultation
        ├── OrdonnanceServiceImpl.java     # Implémentation Ordonnance
        └── MedicamentServiceImpl.java     # Implémentation Medicament
```

---

## ✅ Validation Rules

- Une consultation ne peut pas exister sans `patientId` et `rendezVousId`
- Une ordonnance ne peut pas exister sans une consultation
- Un médicament ne peut pas exister sans une ordonnance
- Soft delete pour Medicament en utilisant le champ `actif`

---

## 🧪 Tests

Des tests unitaires sont fournis pour démontrer les patterns de test utilisés.

Exécuter les tests :
```bash
mvn test
```

---

## 👥 Auteurs

- **Équipe Cabinet Médical**

## 📄 License

Ce projet est sous licence MIT.

