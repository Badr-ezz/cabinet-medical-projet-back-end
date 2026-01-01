package com.cabinet.consultationservice.dto;

import com.cabinet.consultationservice.enums.TypeOrdonnance;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdonnanceRequestDTO {

    @NotNull(message = "Consultation ID is required")
    private Long consultationId;

    @NotNull(message = "Type is required")
    private TypeOrdonnance type;

    private String contenuLibre;
}

