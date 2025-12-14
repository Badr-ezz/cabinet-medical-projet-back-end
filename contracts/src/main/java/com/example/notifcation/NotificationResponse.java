package com.example.notifcation;

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
    private String notificationType;
    private String notificationStatus;
    private LocalDateTime createdAt;
}
