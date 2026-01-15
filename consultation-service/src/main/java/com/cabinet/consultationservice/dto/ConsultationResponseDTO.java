package com.cabinet.consultationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponseDTO {

    private Long id;
    private Long patientId;
    private Long rendezVousId;
    private Long medecinId;
    private String type;
    private LocalDate dateConsultation;
    private String examenClinique;
    private String examenSupplementaire;
    private String diagnostic;
    private String observations;
    private LocalDateTime createdAt;
    @Builder.Default
    private List<OrdonnanceResponseDTO> ordonnances = new ArrayList<>();
}
