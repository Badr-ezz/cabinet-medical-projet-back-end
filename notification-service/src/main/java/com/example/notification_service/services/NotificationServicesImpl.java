package com.example.notification_service.services;

import com.example.notifcation.NotificationResponse;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.NotificationStatus;
import com.example.notification_service.feign.UserServiceClient;
import com.example.notification_service.mapper.EntityToResponse;
import com.example.notification_service.mapper.RequestToEntity;
import com.example.notification_service.repository.NotificationRepo;
import com.example.notification_service.request.NotificationRequest;
import com.example.user.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationServicesImpl implements NotificationServices {

    private final NotificationRepo notificationRepo;
    private final KafkaTemplate<String, NotificationResponse> kafkaTemplate;
    private final UserServiceClient userServiceClient;

    @Autowired
    public NotificationServicesImpl(final NotificationRepo notificationRepo, final KafkaTemplate<String, NotificationResponse> kafkaTemplate, UserServiceClient userServiceClient) {

        this.notificationRepo = notificationRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.userServiceClient = userServiceClient;

    }

    @Override
    public List<NotificationResponse> getNotifications() {
        List<Notification> notifications = notificationRepo.findAll();
        return notifications.stream().map(EntityToResponse::convert).collect(Collectors.toList());
    }

    @Override
    public NotificationResponse addNotification(NotificationRequest notificationRequest) {
        // must check if the user exist or not in the userService

        UserResponse user = userServiceClient.getUser(notificationRequest.getUserId());
        log.info("User: {} ", user.toString());

        Notification notification = RequestToEntity.convert(notificationRequest);
        return EntityToResponse.convert(notificationRepo.save(notification));
    }

    @Override
    public NotificationResponse getNotification(Long notificationId) {
        return null;
    }

    @Override
    public NotificationResponse readNotification(Long notificationId) {
        Optional<Notification> notification = notificationRepo.findById(notificationId);
        if (notification.isPresent()) {
            Notification updatedNotification = notification.get();
            updatedNotification.setNotificationStatus(NotificationStatus.SEEN);

            return EntityToResponse.convert(notificationRepo.save(updatedNotification));
        }
        return NotificationResponse.builder().build();
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest notificationRequest) {
        try {
            NotificationResponse newNotification = addNotification(notificationRequest);
            kafkaTemplate.send("user_notification_topic", newNotification);
            return newNotification;
        } catch (Exception e) {
            System.out.println("---------------------------------------------");
            System.out.println("Error while sending notification" + e.getMessage());
            System.out.println("---------------------------------------------");
            return NotificationResponse.builder()
                    .message(e.getMessage())
                    .build();
        }

    }

}
