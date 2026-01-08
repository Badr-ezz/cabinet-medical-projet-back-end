# Service Cabinet - API Documentation

## 📋 Description

Le **Service Cabinet** est un microservice Spring Boot dédié à la gestion des cabinets médicaux. Il fait partie d'une architecture de projet back-end pour la gestion d'un cabinet médical.

## 🛠️ Technologies Utilisées

| Technologie | Version |
|-------------|---------|
| Java | 17 |
| Spring Boot | 4.0.0 |
| Spring Data JPA | - |
| PostgreSQL | - |
| Apache Kafka | - |
| Lombok | - |
| Maven | - |

## ⚙️ Configuration

### Application Properties

| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `server.port` | 8082 | Port du serveur |
| `spring.application.name` | service-cabinet | Nom de l'application |
| `spring.datasource.url` | jdbc:postgresql://localhost:5432/cabinet-medical-cabinet | URL de la base de données |
| `spring.kafka.bootstrap-servers` | localhost:9092 | Serveur Kafka |

### Prérequis

- Java 17+
- PostgreSQL (port 5432)
- Apache Kafka (optionnel, port 9092)

## 🗂️ Structure du Projet

```
src/main/java/com/example/service_cabinet/
├── ServiceCabinetApplication.java      # Point d'entrée de l'application
├── config/
│   └── KafkaConfig.java                # Configuration Kafka
├── controller/
│   └── CabinetController.java          # Contrôleur REST API
├── dto/
│   ├── CabinetDTO.java                 # Data Transfer Object de réponse
│   └── CreateCabinetRequest.java       # DTO pour création/mise à jour
├── entity/
│   └── Cabinet.java                    # Entité JPA Cabinet
├── repository/
│   └── CabinetRepository.java          # Repository JPA
└── service/
    ├── CabinetService.java             # Logique métier
    └── KafkaProducerService.java       # Service de production Kafka
```

## 📊 Modèle de Données

### Entité Cabinet

| Champ | Type | Description | Contraintes |
|-------|------|-------------|-------------|
| `id` | Long | Identifiant unique | Auto-généré |
| `nom` | String | Nom du cabinet | Obligatoire, max 100 caractères |
| `logo` | String | URL du logo | Optionnel, max 500 caractères |
| `specialite` | String | Spécialité médicale | Max 100 caractères |
| `adresse` | String | Adresse du cabinet | Max 255 caractères |
| `telephone` | String | Numéro de téléphone | Max 20 caractères |
| `email` | String | Adresse email | Max 100 caractères |
| `actif` | Boolean | Statut actif/inactif | Par défaut: true |
| `createdAt` | LocalDateTime | Date de création | Auto-généré |
| `updatedAt` | LocalDateTime | Date de mise à jour | Auto-généré |

## 🚀 API Endpoints

### Base URL
```
http://localhost:8082/api/cabinets
```

### Endpoints Disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/cabinets` | Créer un nouveau cabinet |
| `GET` | `/api/cabinets` | Récupérer tous les cabinets |
| `GET` | `/api/cabinets/active` | Récupérer les cabinets actifs |
| `GET` | `/api/cabinets/{id}` | Récupérer un cabinet par ID |
| `PUT` | `/api/cabinets/{id}` | Mettre à jour un cabinet |
| `DELETE` | `/api/cabinets/{id}` | Désactiver un cabinet (soft delete) |

---

## 📝 Détails des Endpoints

### 1. Créer un Cabinet

**POST** `/api/cabinets`

#### Request Body
```json
{
  "nom": "Cabinet Dentaire Paris",
  "logo": "https://example.com/logo.png",
  "specialite": "Dentisterie",
  "adresse": "123 Rue de Paris, 75001 Paris",
  "telephone": "+33 1 23 45 67 89",
  "email": "contact@cabinet-paris.fr",
  "actif": true
}
```

#### Validation
| Champ | Règle | Message d'erreur |
|-------|-------|------------------|
| `nom` | Obligatoire | "Le nom du cabinet est obligatoire" |
| `specialite` | Obligatoire | "La spécialité est obligatoire" |
| `adresse` | Obligatoire | "L'adresse est obligatoire" |
| `telephone` | Obligatoire | "Le téléphone est obligatoire" |
| `email` | Format email valide | "L'email doit être valide" |
| `actif` | Obligatoire | "Le statut actif est obligatoire" |

#### Response (201 Created)
```json
{
  "id": 1,
  "logo": "https://example.com/logo.png",
  "nom": "Cabinet Dentaire Paris",
  "specialite": "Dentisterie",
  "adresse": "123 Rue de Paris, 75001 Paris",
  "telephone": "+33 1 23 45 67 89",
  "email": "contact@cabinet-paris.fr",
  "actif": true,
  "createdAt": "2026-01-02T10:30:00",
  "updatedAt": "2026-01-02T10:30:00"
}
```

---

### 2. Récupérer Tous les Cabinets

**GET** `/api/cabinets`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "logo": "https://example.com/logo.png",
    "nom": "Cabinet Dentaire Paris",
    "specialite": "Dentisterie",
    "adresse": "123 Rue de Paris, 75001 Paris",
    "telephone": "+33 1 23 45 67 89",
    "email": "contact@cabinet-paris.fr",
    "actif": true,
    "createdAt": "2026-01-02T10:30:00",
    "updatedAt": "2026-01-02T10:30:00"
  }
]
```

---

### 3. Récupérer les Cabinets Actifs

**GET** `/api/cabinets/active`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "logo": "https://example.com/logo.png",
    "nom": "Cabinet Dentaire Paris",
    "specialite": "Dentisterie",
    "adresse": "123 Rue de Paris, 75001 Paris",
    "telephone": "+33 1 23 45 67 89",
    "email": "contact@cabinet-paris.fr",
    "actif": true,
    "createdAt": "2026-01-02T10:30:00",
    "updatedAt": "2026-01-02T10:30:00"
  }
]
```

---

### 4. Récupérer un Cabinet par ID

**GET** `/api/cabinets/{id}`

#### Paramètres
| Paramètre | Type | Description |
|-----------|------|-------------|
| `id` | Long | Identifiant du cabinet |

#### Response (200 OK)
```json
{
  "id": 1,
  "logo": "https://example.com/logo.png",
  "nom": "Cabinet Dentaire Paris",
  "specialite": "Dentisterie",
  "adresse": "123 Rue de Paris, 75001 Paris",
  "telephone": "+33 1 23 45 67 89",
  "email": "contact@cabinet-paris.fr",
  "actif": true,
  "createdAt": "2026-01-02T10:30:00",
  "updatedAt": "2026-01-02T10:30:00"
}
```

#### Response (400 Bad Request) - Cabinet non trouvé
```json
{
  "status": "ERREUR",
  "message": "Cabinet non trouvé avec ID: 999"
}
```

---

### 5. Mettre à Jour un Cabinet

**PUT** `/api/cabinets/{id}`

#### Paramètres
| Paramètre | Type | Description |
|-----------|------|-------------|
| `id` | Long | Identifiant du cabinet |

#### Request Body
```json
{
  "nom": "Cabinet Dentaire Paris - Nouveau",
  "logo": "https://example.com/new-logo.png",
  "specialite": "Dentisterie Générale",
  "adresse": "456 Avenue de Paris, 75002 Paris",
  "telephone": "+33 1 98 76 54 32",
  "email": "nouveau@cabinet-paris.fr",
  "actif": true
}
```

#### Response (200 OK)
```json
{
  "id": 1,
  "logo": "https://example.com/new-logo.png",
  "nom": "Cabinet Dentaire Paris - Nouveau",
  "specialite": "Dentisterie Générale",
  "adresse": "456 Avenue de Paris, 75002 Paris",
  "telephone": "+33 1 98 76 54 32",
  "email": "nouveau@cabinet-paris.fr",
  "actif": true,
  "createdAt": "2026-01-02T10:30:00",
  "updatedAt": "2026-01-02T11:00:00"
}
```

---

### 6. Désactiver un Cabinet (Soft Delete)

**DELETE** `/api/cabinets/{id}`

#### Paramètres
| Paramètre | Type | Description |
|-----------|------|-------------|
| `id` | Long | Identifiant du cabinet |

#### Response (204 No Content)
*Pas de corps de réponse*

> **Note**: Cette opération effectue un "soft delete" - le cabinet n'est pas supprimé de la base de données mais son statut `actif` est mis à `false`.

---

## ❌ Gestion des Erreurs

### Format de Réponse d'Erreur
```json
{
  "status": "ERREUR",
  "message": "Description de l'erreur"
}
```

### Codes d'Erreur Courants

| Code HTTP | Description |
|-----------|-------------|
| 201 | Création réussie |
| 200 | Requête réussie |
| 204 | Suppression réussie (pas de contenu) |
| 400 | Requête invalide / Erreur métier |
| 404 | Ressource non trouvée |

### Messages d'Erreur Métier

| Erreur | Message |
|--------|---------|
| Cabinet déjà existant | "Un cabinet avec ce nom existe déjà" |
| Cabinet non trouvé | "Cabinet non trouvé avec ID: {id}" |

---

## 🔧 Installation et Démarrage

### 1. Cloner le projet
```bash
git clone <repository-url>
cd service-cabinet
```

### 2. Configurer la base de données PostgreSQL
```sql
CREATE DATABASE "cabinet-medical-cabinet";
```

### 3. Configurer les propriétés
Modifier `src/main/resources/application.properties` si nécessaire :
```properties
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
```

### 4. Compiler et lancer l'application
```bash
# Avec Maven Wrapper
./mvnw spring-boot:run

# Ou avec Maven installé
mvn spring-boot:run
```

### 5. Vérifier que le service fonctionne
```bash
curl http://localhost:8082/api/cabinets
```

---

## 📡 Intégration Kafka (Optionnel)

Le service supporte l'intégration avec Apache Kafka pour l'envoi d'événements. Les événements suivants sont émis :

| Événement | Topic | Description |
|-----------|-------|-------------|
| Cabinet Créé | `cabinet-created` | Émis lors de la création d'un cabinet |
| Cabinet Mis à Jour | `cabinet-updated` | Émis lors de la mise à jour d'un cabinet |
| Cabinet Supprimé | `cabinet-deleted` | Émis lors de la désactivation d'un cabinet |

> **Note**: Kafka est désactivé par défaut (`spring.kafka.enabled=false`). Pour l'activer, modifiez cette propriété dans `application.properties`.

---

## 🧪 Tests

Pour exécuter les tests :
```bash
./mvnw test
```

---

## 📄 License

Ce projet est développé dans le cadre d'un projet de cabinet médical.

---

## 👥 Auteurs

Développé avec ❤️ pour la gestion des cabinets médicaux.

