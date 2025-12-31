package com.example.user_service.notificationEvent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
@Component
public class NotificationListener {
    @KafkaListener(topics = "user_notification_topic", groupId = "user-service")
    public void listener(String json) {
        ObjectMapper mapper = new ObjectMapper();
        NotificationMessage message = mapper.readValue(json, NotificationMessage.class);

        System.out.println("RECEIVED: " + message);
    }

}
