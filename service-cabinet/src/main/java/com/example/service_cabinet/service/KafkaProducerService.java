package com.example.service_cabinet.service;

import com.example.service_cabinet.entity.Cabinet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String CABINET_CREATED_TOPIC = "cabinet.created";
    private static final String CABINET_UPDATED_TOPIC = "cabinet.updated";
    private static final String CABINET_DELETED_TOPIC = "cabinet.deleted";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // Envoyer événement création
    public void sendCabinetCreatedEvent(Cabinet cabinet) {
        CabinetEvent event = createCabinetEvent(cabinet, "CREATED");
        sendEvent(CABINET_CREATED_TOPIC, event);
    }

    // Envoyer événement mise à jour
    public void sendCabinetUpdatedEvent(Cabinet cabinet) {
        CabinetEvent event = createCabinetEvent(cabinet, "UPDATED");
        sendEvent(CABINET_UPDATED_TOPIC, event);
    }

    // Envoyer événement suppression
    public void sendCabinetDeletedEvent(Long cabinetId) {
        CabinetDeletedEvent event = new CabinetDeletedEvent(cabinetId, "DELETED", System.currentTimeMillis());
        sendEvent(CABINET_DELETED_TOPIC, event);
    }

    // Méthode générique pour envoyer
    private <T> void sendEvent(String topic, T event) {
        try {
            String eventMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, eventMessage);
            System.out.println("✓ Événement Kafka envoyé au topic: " + topic);
        } catch (Exception e) {
            System.err.println("✗ Erreur lors de l'envoi du message Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Créer événement cabinet
    private CabinetEvent createCabinetEvent(Cabinet cabinet, String eventType) {
        CabinetEvent event = new CabinetEvent();
        event.setEventType(eventType);
        event.setCabinetId(cabinet.getId());
        event.setCabinetName(cabinet.getNom());
        event.setSpecialite(cabinet.getSpecialite());
        event.setAdresse(cabinet.getAdresse());
        event.setTelephone(cabinet.getTelephone());
        event.setEmail(cabinet.getEmail());
        event.setActif(cabinet.getActif());
        event.setTimestamp(System.currentTimeMillis());
        return event;
    }

    // ===== CLASSES INTERNES POUR LES ÉVÉNEMENTS =====

    public static class CabinetEvent {
        private String eventType;
        private Long cabinetId;
        private String cabinetName;
        private String specialite;
        private String adresse;
        private String telephone;
        private String email;
        private Boolean actif;
        private Long timestamp;

        public CabinetEvent() {
        }

        public CabinetEvent(String eventType, Long cabinetId, String cabinetName) {
            this.eventType = eventType;
            this.cabinetId = cabinetId;
            this.cabinetName = cabinetName;
            this.timestamp = System.currentTimeMillis();
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Long getCabinetId() {
            return cabinetId;
        }

        public void setCabinetId(Long cabinetId) {
            this.cabinetId = cabinetId;
        }

        public String getCabinetName() {
            return cabinetName;
        }

        public void setCabinetName(String cabinetName) {
            this.cabinetName = cabinetName;
        }

        public String getSpecialite() {
            return specialite;
        }

        public void setSpecialite(String specialite) {
            this.specialite = specialite;
        }

        public String getAdresse() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getActif() {
            return actif;
        }

        public void setActif(Boolean actif) {
            this.actif = actif;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "CabinetEvent{" +
                    "eventType='" + eventType + '\'' +
                    ", cabinetId=" + cabinetId +
                    ", cabinetName='" + cabinetName + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }

    public static class CabinetDeletedEvent {
        private Long cabinetId;
        private String eventType;
        private Long timestamp;

        public CabinetDeletedEvent() {
        }

        public CabinetDeletedEvent(Long cabinetId, String eventType, Long timestamp) {
            this.cabinetId = cabinetId;
            this.eventType = eventType;
            this.timestamp = timestamp;
        }

        public Long getCabinetId() {
            return cabinetId;
        }

        public void setCabinetId(Long cabinetId) {
            this.cabinetId = cabinetId;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "CabinetDeletedEvent{" +
                    "cabinetId=" + cabinetId +
                    ", eventType='" + eventType + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
