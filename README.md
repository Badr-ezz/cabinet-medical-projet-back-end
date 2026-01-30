# 🏥 Cabinet Médical – Backend Microservices

Backend du système de gestion d’un cabinet médical basé sur une architecture microservices avec Spring Boot.

Ce projet fournit une plateforme backend scalable permettant de gérer patients, rendez-vous, consultations, dossiers médicaux, notifications et authentification sécurisée.

---

## 🎯 Objectif du Projet

Construire un backend distribué capable de :

- gérer plusieurs rôles utilisateurs
- assurer la sécurité des données médicales
- séparer les responsabilités par microservices
- garantir la traçabilité des opérations
- permettre un déploiement scalable
- intégrer un système de notifications temps réel

Le projet applique des principes d’architecture cloud-native modernes.

---

## 🧱 Architecture Microservices

Frontend Angular
↓
API Gateway
↓
Microservices Spring Boot
↓
Databases + Kafka Event Bus


Composants principaux :

- Service Discovery (Eureka)
- API Gateway
- Services métier indépendants
- Messaging Kafka
- Bases de données séparées par service

---

## 🧩 Microservices du système

| Service | Responsabilité |
|--------|----------------|
| registry-service | Service Discovery (Eureka) |
| api-gateway | Point d’entrée unique |
| user-service | Authentification + gestion utilisateurs |
| patient-service | Gestion des patients |
| rendezvous-service | Gestion des rendez-vous |
| consultation-service | Gestion des consultations |
| notification-service | Notifications asynchrones |
| service-cabinet | Gestion des cabinets |

Chaque service possède :

- sa base de données dédiée
- sa logique métier isolée
- son cycle de déploiement indépendant

---

## 🔄 Communication entre services

- REST pour les appels synchrones
- Kafka pour les événements asynchrones
- Gateway pour le routage
- JWT pour la sécurité

Principes :

- découplage fort
- scalabilité horizontale
- tolérance aux pannes
- architecture événementielle

---

## 🔐 Sécurité

- authentification JWT
- gestion des rôles
- isolation des services
- contrôle d’accès par Gateway
- secrets externalisés

---

## 🧪 Qualité & Tests

Le backend intègre :

- tests unitaires JUnit
- mocks Mockito
- tests d’intégration
- analyse SonarQube
- pipeline CI/CD Jenkins
- tests automatisés

---

## 📌 Stack Technique

- Java 17 / 21
- Spring Boot
- Spring Cloud
- Eureka
- API Gateway
- Kafka
- PostgreSQL / MySQL
- Docker
- Maven

---

## 📈 Concepts DevOps démontrés

- architecture microservices
- event-driven architecture
- service discovery
- containerisation
- CI/CD
- séparation base de données par service
- scaling distribué

---

## 📂 Structure du Projet

Voir section *Structure du projet* ci-dessous.

---

