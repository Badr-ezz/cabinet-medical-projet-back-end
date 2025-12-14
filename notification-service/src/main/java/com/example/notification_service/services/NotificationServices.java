package com.example.notification_service.services;

import com.example.notifcation.NotificationResponse;
import com.example.notification_service.request.NotificationRequest;

import java.util.List;

public interface NotificationServices {
    List<NotificationResponse> getNotifications();
    NotificationResponse addNotification(NotificationRequest notificationRequest);
    NotificationResponse getNotification(Long notificationId);
    NotificationResponse readNotification(Long notificationId);
    NotificationResponse sendNotification(NotificationRequest notificationRequest);
}
