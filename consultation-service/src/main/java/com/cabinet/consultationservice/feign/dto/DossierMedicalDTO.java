package com.cabinet.consultationservice.feign.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DossierMedicalDTO {
    private Long idDossier;
    private String antecedentsMedicaux;
    private String antecedentsChirurgicaux;
    private String allergies;
    private String traitements;
    private String habitudes;
    private String documentsMedicaux;
    private LocalDate dateCreation;
    private Long patientId;
}

