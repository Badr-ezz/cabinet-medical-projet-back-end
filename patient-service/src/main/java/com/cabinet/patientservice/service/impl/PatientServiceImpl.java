package com.cabinet.patientservice.service.impl;

import com.cabinet.patientservice.dto.PatientRequestDTO;
import com.cabinet.patientservice.dto.PatientResponseDTO;
import com.cabinet.patientservice.exception.DuplicateResourceException;
import com.cabinet.patientservice.exception.ResourceNotFoundException;
import com.cabinet.patientservice.mapper.PatientMapper;
import com.cabinet.patientservice.model.DossierMedical;
import com.cabinet.patientservice.model.Patient;
import com.cabinet.patientservice.repository.DossierMedicalRepository;
import com.cabinet.patientservice.repository.PatientRepository;
import com.cabinet.patientservice.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              DossierMedicalRepository dossierMedicalRepository) {
        this.patientRepository = patientRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        if (patientRepository.existsByCin(requestDTO.getCin())) {
            throw new DuplicateResourceException("Patient with CIN " + requestDTO.getCin() + " already exists");
        }

        Patient patient = PatientMapper.toEntity(requestDTO);
        // Create empty dossier medical automatically
        DossierMedical dossier = DossierMedical.builder()
                .dateCreation(LocalDate.now())
                .patient(patient)
                .build();
        patient.setDossierMedical(dossier);

        Patient saved = patientRepository.save(patient);
        return PatientMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));
        return PatientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));

        // If CIN is changed, ensure uniqueness
        if (!patient.getCin().equals(requestDTO.getCin())
                && patientRepository.existsByCin(requestDTO.getCin())) {
            throw new DuplicateResourceException("Patient with CIN " + requestDTO.getCin() + " already exists");
        }

        PatientMapper.updateEntityFromDto(requestDTO, patient);
        Patient updated = patientRepository.save(patient);
        return PatientMapper.toResponseDto(updated);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));
        // DossierMedical will be deleted because of orphanRemoval and cascade
        patientRepository.delete(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientByCin(String cin) {
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with CIN " + cin + " not found"));
        return PatientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> searchPatientsByNom(String nom) {
        return patientRepository.findByNomIgnoreCaseContaining(nom).stream()
                .map(PatientMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}



