package com.example.notification_service.mapper;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.request.NotificationRequest;
import com.example.notification_service.response.NotificationResponse;
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
                .notificationType(entity.getNotificationType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}