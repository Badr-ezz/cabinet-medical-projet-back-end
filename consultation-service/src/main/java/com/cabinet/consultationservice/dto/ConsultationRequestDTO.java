package com.cabinet.consultationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsultationRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Rendez-vous ID is required")
    private Long rendezVousId;

    @NotNull(message = "Medecin ID is required")
    private Long medecinId;

    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type; // CONSULTATION or CONTROLE

    private LocalDate dateConsultation;

    private String examenClinique;

    private String examenSupplementaire;

    private String diagnostic;

    private String observations;

    private java.util.List<MedicamentDTO> medicaments;
}
