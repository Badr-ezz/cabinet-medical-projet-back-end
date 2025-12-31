package com.cabinet.consultationservice.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureRequestDTO {
    private Long consultationId;
    private Long patientId;
    private Double montant;
    private String modePaiement; // ESPECES, CARTE, ASSURANCE
}

