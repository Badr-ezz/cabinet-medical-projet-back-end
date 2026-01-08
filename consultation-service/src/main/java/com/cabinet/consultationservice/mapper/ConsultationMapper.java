package com.cabinet.consultationservice.mapper;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.model.Consultation;

import java.util.stream.Collectors;

public class ConsultationMapper {

    private ConsultationMapper() {
    }

    public static Consultation toEntity(ConsultationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Consultation.builder()
                .patientId(dto.getPatientId())
                .rendezVousId(dto.getRendezVousId())
                .medecinId(dto.getMedecinId())
                .type(dto.getType())
                .dateConsultation(dto.getDateConsultation())
                .examenClinique(dto.getExamenClinique())
                .examenSupplementaire(dto.getExamenSupplementaire())
                .diagnostic(dto.getDiagnostic())
                .observations(dto.getObservations())
                .build();
    }

    public static ConsultationResponseDTO toResponseDto(Consultation consultation) {
        if (consultation == null) {
            return null;
        }
        ConsultationResponseDTO dto = ConsultationResponseDTO.builder()
                .id(consultation.getId())
                .patientId(consultation.getPatientId())
                .rendezVousId(consultation.getRendezVousId())
                .medecinId(consultation.getMedecinId())
                .type(consultation.getType())
                .dateConsultation(consultation.getDateConsultation())
                .examenClinique(consultation.getExamenClinique())
                .examenSupplementaire(consultation.getExamenSupplementaire())
                .diagnostic(consultation.getDiagnostic())
                .observations(consultation.getObservations())
                .createdAt(consultation.getCreatedAt())
                .build();

        if (consultation.getOrdonnances() != null && !consultation.getOrdonnances().isEmpty()) {
            dto.setOrdonnances(consultation.getOrdonnances().stream()
                    .map(OrdonnanceMapper::toResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}

