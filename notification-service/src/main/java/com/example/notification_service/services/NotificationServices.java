package com.example.notification_service.services;

import com.example.notification_service.request.NotificationRequest;
import com.example.notification_service.response.NotificationResponse;

import java.util.List;

public interface NotificationServices {
    List<NotificationResponse> getNotifications();
    NotificationResponse addNotification(NotificationRequest notificationRequest);
    NotificationResponse getNotification(String notificationId);
    void deleteNotification(String notificationId);
    void sendNotification(NotificationRequest notificationRequest);
}
