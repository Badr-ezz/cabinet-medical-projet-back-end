package com.example.notification_service.controller;


import com.example.notification_service.request.NotificationRequest;
import com.example.notification_service.response.NotificationResponse;
import com.example.notification_service.services.NotificationServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationServices notificationServices;

    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }


    @GetMapping("/send-notification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        try {
            notificationServices.sendNotification(notificationRequest);
            return ResponseEntity.ok().body("Notification envoyée avec succes!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
