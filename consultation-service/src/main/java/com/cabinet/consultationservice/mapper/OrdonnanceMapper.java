package com.cabinet.consultationservice.mapper;

import com.cabinet.consultationservice.dto.OrdonnanceRequestDTO;
import com.cabinet.consultationservice.dto.OrdonnanceResponseDTO;
import com.cabinet.consultationservice.model.Ordonnance;

import java.util.stream.Collectors;

public class OrdonnanceMapper {

    private OrdonnanceMapper() {
    }

    public static Ordonnance toEntity(OrdonnanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Ordonnance.builder()
                .type(dto.getType())
                .contenuLibre(dto.getContenuLibre())
                .build();
    }

    public static OrdonnanceResponseDTO toResponseDto(Ordonnance ordonnance) {
        if (ordonnance == null) {
            return null;
        }
        OrdonnanceResponseDTO dto = OrdonnanceResponseDTO.builder()
                .id(ordonnance.getId())
                .type(ordonnance.getType())
                .contenuLibre(ordonnance.getContenuLibre())
                .createdAt(ordonnance.getCreatedAt())
                .build();

        if (ordonnance.getConsultation() != null) {
            dto.setConsultationId(ordonnance.getConsultation().getId());
        }

        if (ordonnance.getMedicaments() != null && !ordonnance.getMedicaments().isEmpty()) {
            dto.setMedicaments(ordonnance.getMedicaments().stream()
                    .map(MedicamentMapper::toResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}

