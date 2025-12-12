package com.example.user_service.notificationEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private Long id;

    private Long userId;

    private String message;

    private NotificationType notificationType;

    private LocalDateTime createdAt;
}

