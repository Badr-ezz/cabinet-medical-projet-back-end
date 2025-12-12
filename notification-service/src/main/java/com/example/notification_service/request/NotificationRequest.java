package com.example.notification_service.request;

import com.example.notification_service.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {


    private Long id;

    private Long userId;

    private String message;

    private NotificationType notificationType;


}
