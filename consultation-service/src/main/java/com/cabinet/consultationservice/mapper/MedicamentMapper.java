package com.cabinet.consultationservice.mapper;

import com.cabinet.consultationservice.dto.MedicamentRequestDTO;
import com.cabinet.consultationservice.dto.MedicamentResponseDTO;
import com.cabinet.consultationservice.model.Medicament;

public class MedicamentMapper {

    private MedicamentMapper() {
    }

    public static Medicament toEntity(MedicamentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Medicament.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .dosage(dto.getDosage())
                .duree(dto.getDuree())
                .actif(true)
                .build();
    }

    public static MedicamentResponseDTO toResponseDto(Medicament medicament) {
        if (medicament == null) {
            return null;
        }
        MedicamentResponseDTO dto = MedicamentResponseDTO.builder()
                .id(medicament.getId())
                .nom(medicament.getNom())
                .description(medicament.getDescription())
                .dosage(medicament.getDosage())
                .duree(medicament.getDuree())
                .actif(medicament.getActif())
                .build();

        if (medicament.getOrdonnance() != null) {
            dto.setOrdonnanceId(medicament.getOrdonnance().getId());
        }

        return dto;
    }
}

