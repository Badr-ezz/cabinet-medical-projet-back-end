package com.example.notification_service.mapper;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.request.NotificationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestToEntity {
    public static Notification convert(final NotificationRequest request) {
        return Notification.builder()
                .message(request.getMessage())
                .userId(request.getUserId())
                .notificationType(request.getNotificationType())
                .build();
    }
}
