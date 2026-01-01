package com.cabinet.patientservice.mapper;

import com.cabinet.patientservice.dto.PatientRequestDTO;
import com.cabinet.patientservice.model.Patient;
import com.example.patient.PatientResponseDTO;

public class PatientMapper {

    private PatientMapper() {
    }

    public static Patient toEntity(PatientRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Patient.builder()
                .cin(dto.getCin())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .dateNaissance(dto.getDateNaissance())
                .sexe(dto.getSexe())
                .numTel(dto.getNumTel())
                .typeMutuelle(dto.getTypeMutuelle())
                .build();
    }

    public static void updateEntityFromDto(PatientRequestDTO dto, Patient patient) {
        if (dto == null || patient == null) {
            return;
        }
        patient.setCin(dto.getCin());
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setSexe(dto.getSexe());
        patient.setNumTel(dto.getNumTel());
        patient.setTypeMutuelle(dto.getTypeMutuelle());
    }

    public static PatientResponseDTO toResponseDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
        dto.setCin(patient.getCin());
        dto.setNom(patient.getNom());
        dto.setPrenom(patient.getPrenom());
        dto.setDateNaissance(patient.getDateNaissance());
        dto.setSexe(patient.getSexe());
        dto.setNumTel(patient.getNumTel());
        dto.setTypeMutuelle(patient.getTypeMutuelle());
        return dto;
    }
}


