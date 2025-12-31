package com.cabinet.consultationservice.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousResponseDTO {
    private Long idRendezVous;
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String motif;
    private String statut; // StatutRDV enum as String
    private String notes;
    private Long medecinId;
    private Long patientId;
    private Long cabinetId;
}

