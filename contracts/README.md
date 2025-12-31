# Contracts Module

A shared library containing Data Transfer Objects (DTOs) used across microservices in the **Cabinet Medical** backend project.

## 📋 Overview

This module serves as a **contracts library** that provides common DTOs (Response classes) to ensure consistency and avoid code duplication between microservices. Any service that needs to communicate or share data models can depend on this module.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Microservices                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │ User Service │  │ Notification │  │    Other     │       │
│  │              │  │   Service    │  │   Services   │       │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘       │
│         │                 │                 │               │
│         └─────────────────┼─────────────────┘               │
│                           │                                 │
│                           ▼                                 │
│               ┌───────────────────────┐                     │
│               │   Contracts Module    │                     │
│               │   (Shared DTOs)       │                     │
│               └───────────────────────┘                     │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
contracts/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    ├── user/
                    │   └── UserResponse.java
                    └── notifcation/
                        └── NotificationResponse.java
```

## 📦 DTOs (Data Transfer Objects)

### UserResponse

Response DTO representing user information.

| Field       | Type     | Description                        |
|-------------|----------|------------------------------------|
| `id`        | `Long`   | Unique identifier of the user      |
| `login`     | `String` | User login/username                |
| `nom`       | `String` | User's last name                   |
| `prenom`    | `String` | User's first name                  |
| `signature` | `String` | User's signature                   |
| `numTel`    | `String` | User's phone number                |
| `role`      | `String` | User's role (e.g., DOCTOR, ADMIN)  |

**Location:** `com.example.user.UserResponse`

```java
UserResponse user = UserResponse.builder()
    .id(1L)
    .login("dr.smith")
    .nom("Smith")
    .prenom("John")
    .role("DOCTOR")
    .build();
```

---

### NotificationResponse

Response DTO representing notification information.

| Field                | Type            | Description                              |
|----------------------|-----------------|------------------------------------------|
| `id`                 | `Long`          | Unique identifier of the notification    |
| `userId`             | `Long`          | ID of the user receiving the notification|
| `message`            | `String`        | Notification message content             |
| `notificationType`   | `String`        | Type of notification                     |
| `notificationStatus` | `String`        | Status (e.g., READ, UNREAD)              |
| `createdAt`          | `LocalDateTime` | Timestamp when notification was created  |

**Location:** `com.example.notifcation.NotificationResponse`

```java
NotificationResponse notification = NotificationResponse.builder()
    .id(1L)
    .userId(100L)
    .message("Your appointment is confirmed")
    .notificationType("APPOINTMENT")
    .notificationStatus("UNREAD")
    .createdAt(LocalDateTime.now())
    .build();
```

## 🔧 Technologies & Dependencies

| Dependency              | Version   | Purpose                                      |
|-------------------------|-----------|----------------------------------------------|
| **Lombok**              | 1.18.34   | Reduces boilerplate (getters, setters, builders) |
| **Jakarta Validation**  | 3.1.0     | Validation annotations for DTOs              |
| **JUnit**               | 3.8.1     | Unit testing                                 |

## 🚀 Usage

### Adding as a Dependency

To use this module in other microservices, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>contracts</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Building the Module

```bash
mvn clean install
```

This will compile the module and install it to your local Maven repository, making it available for other services.

## 📝 Notes

- All DTOs use **Lombok** annotations (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) to minimize boilerplate code.
- The module is packaged as a **JAR** file for easy distribution across services.
- DTOs follow the **Response** naming convention (e.g., `UserResponse`, `NotificationResponse`).

## 🔮 Future Enhancements

- [ ] Add Request DTOs for API input validation
- [ ] Add more domain-specific DTOs (e.g., `AppointmentResponse`, `PatientResponse`)
- [ ] Add validation constraints using Jakarta Validation annotations

---

**Project:** Cabinet Medical Backend  
**Module:** Contracts (Shared DTOs)  
**Version:** 1.0-SNAPSHOT

