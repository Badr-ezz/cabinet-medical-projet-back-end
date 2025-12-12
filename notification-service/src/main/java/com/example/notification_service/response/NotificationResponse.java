package com.example.notification_service.response;

import com.example.notification_service.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;

    private Long userId;

    private String message;

    private NotificationType notificationType;

    private LocalDateTime createdAt;
}
