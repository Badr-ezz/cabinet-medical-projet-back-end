package com.example.notification_service.mapper;

import com.example.notifcation.NotificationResponse;
import com.example.notification_service.entity.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityToResponse {

    public static NotificationResponse convert(final Notification entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .userId(entity.getUserId())
                .notificationType(entity.getNotificationType().toString())
                .createdAt(entity.getCreatedAt())
                .notificationStatus(entity.getNotificationStatus().toString())
                .build();
    }

}