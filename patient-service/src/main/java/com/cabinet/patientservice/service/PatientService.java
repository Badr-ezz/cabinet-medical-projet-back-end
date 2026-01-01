package com.cabinet.patientservice.service;

import com.cabinet.patientservice.dto.PatientRequestDTO;
import com.example.patient.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO createPatient(PatientRequestDTO requestDTO);

    PatientResponseDTO getPatientById(Long id);

    List<PatientResponseDTO> getAllPatients();

    PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO);

    void deletePatient(Long id);

    PatientResponseDTO getPatientByCin(String cin);

    List<PatientResponseDTO> searchPatientsByNom(String nom);
}



