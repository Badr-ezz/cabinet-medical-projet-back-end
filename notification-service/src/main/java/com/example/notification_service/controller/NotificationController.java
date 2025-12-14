package com.example.notification_service.controller;


import com.example.notifcation.NotificationResponse;
import com.example.notification_service.request.NotificationRequest;
import com.example.notification_service.services.NotificationServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationServices notificationServices;

    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }


    @PostMapping("/send-notification")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        try {
            NotificationResponse res = notificationServices.sendNotification(notificationRequest);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse
                            .builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/read-notification/{notId}")
    public ResponseEntity<NotificationResponse> readNotification(@PathVariable Long notId) {

        try {
            return ResponseEntity.ok().body(notificationServices.readNotification(notId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(notificationServices.readNotification(notId));
        }
    }

}
