package com.example.user_service.notificationEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
@Component
public class NotificationListener {
    @KafkaListener(topics = "user_notification_topic", groupId = "user-service")
    public void listener(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            NotificationMessage message = mapper.readValue(json, NotificationMessage.class);
            System.out.println("RECEIVED: " + message);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing notification message: " + e.getMessage());
        }
    }

}
