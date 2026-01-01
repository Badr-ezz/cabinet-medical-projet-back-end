package com.cabinet.consultationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentResponseDTO {

    private Long id;
    private String nom;
    private String description;
    private String dosage;
    private String duree;
    private Boolean actif;
    private Long ordonnanceId;
}

