package com.example.service_cabinet.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConfig {

    @Bean
    public NewTopic cabinetCreatedTopic() {
        return TopicBuilder.name("cabinet.created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cabinetUpdatedTopic() {
        return TopicBuilder.name("cabinet.updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cabinetDeletedTopic() {
        return TopicBuilder.name("cabinet.deleted")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
