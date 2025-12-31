package com.cabinet.consultationservice.dto;

import com.cabinet.consultationservice.feign.dto.DossierMedicalDTO;
import com.cabinet.consultationservice.feign.dto.PatientResponseDTO;
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
public class ConsultationWithPatientDataDTO {
    // Consultation data
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
    
    // Enriched patient data
    private PatientResponseDTO patient;
    private DossierMedicalDTO dossierMedical;
    
    // Ordonnances
    @Builder.Default
    private List<OrdonnanceResponseDTO> ordonnances = new ArrayList<>();
}

