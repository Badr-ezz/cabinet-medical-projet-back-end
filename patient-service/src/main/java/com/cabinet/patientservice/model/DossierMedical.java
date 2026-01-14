package com.cabinet.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "dossiers_medicaux")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDossier;

    @Column(columnDefinition = "TEXT")
    private String antecedentsMedicaux;

    @Column(columnDefinition = "TEXT")
    private String antecedentsChirurgicaux;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String traitements;

    @Column(columnDefinition = "TEXT")
    private String habitudes;

    @Column(columnDefinition = "TEXT")
    private String documentsMedicaux;

    private LocalDate dateCreation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;
}


