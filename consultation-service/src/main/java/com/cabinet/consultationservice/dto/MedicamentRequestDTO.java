package com.cabinet.consultationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicamentRequestDTO {

    @NotNull(message = "Ordonnance ID is required")
    private Long ordonnanceId;

    @NotBlank(message = "Nom is required")
    @Size(max = 200, message = "Nom must not exceed 200 characters")
    private String nom;

    private String description;

    @Size(max = 100, message = "Dosage must not exceed 100 characters")
    private String dosage;

    @Size(max = 100, message = "Duree must not exceed 100 characters")
    private String duree;
}

