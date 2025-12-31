# 🏥 Patient Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Description

Le **Patient Service** est un microservice faisant partie du projet de gestion d'un cabinet médical. Ce service est responsable de la gestion complète des patients et de leurs dossiers médicaux.

## 🏗️ Architecture

Ce projet suit une **architecture microservices** avec les caractéristiques suivantes :

```
patient-service/
├── src/main/java/com/cabinet/patientservice/
│   ├── config/                 # Configuration (OpenAPI, Exception Handler)
│   ├── controller/             # Contrôleurs REST (API endpoints)
│   ├── dto/                    # Data Transfer Objects (Request/Response)
│   ├── exception/              # Exceptions personnalisées
│   ├── mapper/                 # Mappers Entity <-> DTO
│   ├── model/                  # Entités JPA
│   ├── repository/             # Repositories Spring Data JPA
│   └── service/                # Services métier
│       └── impl/               # Implémentations des services
└── src/main/resources/
    └── application.yml         # Configuration de l'application
```

### 🔄 Pattern Utilisé

- **Layered Architecture** (Architecture en couches)
- **DTO Pattern** (Séparation Request/Response)
- **Repository Pattern** avec Spring Data JPA
- **Dependency Injection** via Spring

---

## 🗃️ Entités (Entities)

### 📌 Patient

| Champ          | Type        | Contraintes                    | Description                |
|----------------|-------------|--------------------------------|----------------------------|
| `id`           | Long        | PK, Auto-generated             | Identifiant unique         |
| `cin`          | String(50)  | NOT NULL, UNIQUE               | Carte d'identité nationale |
| `nom`          | String(100) | NOT NULL                       | Nom du patient             |
| `prenom`       | String(100) | NOT NULL                       | Prénom du patient          |
| `dateNaissance`| LocalDate   | Nullable                       | Date de naissance          |
| `sexe`         | String(10)  | Nullable                       | Sexe du patient            |
| `numTel`       | String(20)  | Nullable                       | Numéro de téléphone        |
| `typeMutuelle` | String(100) | Nullable                       | Type de mutuelle           |

**Relation :** `OneToOne` avec `DossierMedical` (Un patient possède un dossier médical)

### 📌 DossierMedical

| Champ                   | Type        | Contraintes        | Description                     |
|-------------------------|-------------|--------------------|---------------------------------|
| `idDossier`             | Long        | PK, Auto-generated | Identifiant unique du dossier   |
| `antecedentsMedicaux`   | TEXT        | Nullable           | Antécédents médicaux            |
| `antecedentsChirurgicaux`| TEXT       | Nullable           | Antécédents chirurgicaux        |
| `allergies`             | TEXT        | Nullable           | Allergies connues               |
| `traitements`           | TEXT        | Nullable           | Traitements en cours            |
| `habitudes`             | TEXT        | Nullable           | Habitudes (tabac, alcool, etc.) |
| `documentsMedicaux`     | TEXT        | Nullable           | Documents médicaux associés     |
| `dateCreation`          | LocalDate   | Nullable           | Date de création du dossier     |
| `patient_id`            | Long        | FK, NOT NULL, UNIQUE| Référence vers le patient      |

---

## 📦 DTOs (Data Transfer Objects)

### ➡️ PatientRequestDTO (Requête)

Utilisé pour la création et la mise à jour d'un patient.

```java
{
    "cin": "AB123456",          // @NotBlank, @Size(max=50)
    "nom": "Dupont",            // @NotBlank, @Size(max=100)
    "prenom": "Jean",           // @NotBlank, @Size(max=100)
    "dateNaissance": "1990-05-15",
    "sexe": "Masculin",
    "numTel": "0612345678",
    "typeMutuelle": "CNSS"
}
```

### ⬅️ PatientResponseDTO (Réponse)

Retourné par l'API lors des opérations de lecture.

```java
{
    "id": 1,
    "cin": "AB123456",
    "nom": "Dupont",
    "prenom": "Jean",
    "dateNaissance": "1990-05-15",
    "sexe": "Masculin",
    "numTel": "0612345678",
    "typeMutuelle": "CNSS"
}
```

### 📋 DossierMedicalDTO

Utilisé pour les opérations sur le dossier médical (Request & Response).

```java
{
    "idDossier": 1,
    "antecedentsMedicaux": "Diabète type 2",
    "antecedentsChirurgicaux": "Appendicectomie 2015",
    "allergies": "Pénicilline",
    "traitements": "Metformine 500mg",
    "habitudes": "Non fumeur",
    "documentsMedicaux": "Radio thorax 2024",
    "dateCreation": "2024-01-15",
    "patientId": 1
}
```

---

## 🌐 API Endpoints

### 👤 Patient Controller

Base URL: `/api/patients`

| Méthode | Endpoint            | Description                          | Request Body        | Response            |
|---------|---------------------|--------------------------------------|---------------------|---------------------|
| `POST`  | `/api/patients`     | Créer un nouveau patient             | PatientRequestDTO   | PatientResponseDTO  |
| `GET`   | `/api/patients`     | Récupérer tous les patients          | -                   | List\<PatientResponseDTO\> |
| `GET`   | `/api/patients/{id}`| Récupérer un patient par ID          | -                   | PatientResponseDTO  |
| `GET`   | `/api/patients/cin/{cin}` | Récupérer un patient par CIN    | -                   | PatientResponseDTO  |
| `GET`   | `/api/patients/search?nom={nom}` | Rechercher patients par nom | -              | List\<PatientResponseDTO\> |
| `PUT`   | `/api/patients/{id}`| Mettre à jour un patient             | PatientRequestDTO   | PatientResponseDTO  |
| `DELETE`| `/api/patients/{id}`| Supprimer un patient                 | -                   | 204 No Content      |

### 📁 Dossier Medical Controller

Base URL: `/api/dossiers`

| Méthode | Endpoint                        | Description                              | Request Body       | Response            |
|---------|---------------------------------|------------------------------------------|--------------------|---------------------|
| `GET`   | `/api/dossiers/patient/{patientId}` | Récupérer le dossier d'un patient   | -                  | DossierMedicalDTO   |
| `PUT`   | `/api/dossiers/patient/{patientId}` | Mettre à jour le dossier d'un patient| DossierMedicalDTO | DossierMedicalDTO   |

---

## ⚙️ Configuration

### Application Properties (`application.yml`)

```yaml
spring:
  application:
    name: patient-service
  datasource:
    url: jdbc:mysql://localhost:3306/patient_service_db
    username: patient_app
    password: patient123
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8081
```

### 📊 Base de données

- **Type:** MySQL 8.0
- **Database:** `patient_service_db`
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
   cd patient-service
   ```

2. **Configurer la base de données MySQL**
   ```sql
   CREATE DATABASE patient_service_db;
   CREATE USER 'patient_app'@'localhost' IDENTIFIED BY 'patient123';
   GRANT ALL PRIVILEGES ON patient_service_db.* TO 'patient_app'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Compiler et lancer l'application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Accéder à l'application**
   - API: `http://localhost:8081/api/patients`
   - Swagger UI: `http://localhost:8081/swagger-ui.html`

---

## 📚 Documentation API (Swagger)

L'API est documentée avec **OpenAPI 3.0 / Swagger UI**.

Accéder à la documentation interactive :
```
http://localhost:8081/swagger-ui.html
```

---

## 🛠️ Technologies Utilisées

| Technologie               | Version | Description                          |
|---------------------------|---------|--------------------------------------|
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
| `DuplicateResourceException`| 409        | Ressource dupliquée (ex: CIN existe) |
| `ValidationException`      | 400         | Erreur de validation                 |

---

## 📂 Structure des Packages

```
com.cabinet.patientservice
├── PatientServiceApplication.java    # Point d'entrée
├── config
│   ├── GlobalExceptionHandler.java   # Gestion globale des exceptions
│   └── OpenAPIConfig.java            # Configuration Swagger
├── controller
│   ├── PatientController.java        # REST endpoints Patient
│   └── DossierMedicalController.java # REST endpoints Dossier
├── dto
│   ├── PatientRequestDTO.java        # DTO requête Patient
│   ├── PatientResponseDTO.java       # DTO réponse Patient
│   └── DossierMedicalDTO.java        # DTO Dossier Médical
├── exception
│   ├── ApiError.java                 # Format erreur API
│   ├── DuplicateResourceException.java
│   └── ResourceNotFoundException.java
├── mapper
│   ├── PatientMapper.java            # Mapper Patient
│   └── DossierMedicalMapper.java     # Mapper Dossier
├── model
│   ├── Patient.java                  # Entité Patient
│   └── DossierMedical.java           # Entité Dossier Médical
├── repository
│   ├── PatientRepository.java        # Repository Patient
│   └── DossierMedicalRepository.java # Repository Dossier
└── service
    ├── PatientService.java           # Interface Service Patient
    ├── DossierMedicalService.java    # Interface Service Dossier
    └── impl
        ├── PatientServiceImpl.java   # Implémentation Patient
        └── DossierMedicalServiceImpl.java # Implémentation Dossier
```

---

## 👥 Auteurs

- **Équipe Cabinet Médical**

## 📄 License

Ce projet est sous licence MIT.

