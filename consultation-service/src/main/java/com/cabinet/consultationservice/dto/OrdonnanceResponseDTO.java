package com.cabinet.consultationservice.dto;

import com.cabinet.consultationservice.enums.TypeOrdonnance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdonnanceResponseDTO {

    private Long id;
    private TypeOrdonnance type;
    private String contenuLibre;
    private Long consultationId;
    private LocalDateTime createdAt;
    @Builder.Default
    private List<MedicamentResponseDTO> medicaments = new ArrayList<>();
}

