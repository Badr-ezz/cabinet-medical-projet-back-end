package com.cabinet.consultationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long rendezVousId;

    @Column(nullable = false)
    private Long medecinId;

    @Column(nullable = false, length = 50)
    private String type; // CONSULTATION or CONTROLE

    @Column(nullable = false)
    private LocalDate dateConsultation;

    @Column(columnDefinition = "TEXT")
    private String examenClinique;

    @Column(columnDefinition = "TEXT")
    private String examenSupplementaire;

    @Column(columnDefinition = "TEXT")
    private String diagnostic;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ordonnance> ordonnances = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dateConsultation == null) {
            dateConsultation = LocalDate.now();
        }
    }
}
