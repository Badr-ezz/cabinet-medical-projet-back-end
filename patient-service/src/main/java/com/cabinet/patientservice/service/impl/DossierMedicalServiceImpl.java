package com.cabinet.patientservice.service.impl;

import com.cabinet.patientservice.dto.DossierMedicalDTO;
import com.cabinet.patientservice.exception.ResourceNotFoundException;
import com.cabinet.patientservice.mapper.DossierMedicalMapper;
import com.cabinet.patientservice.model.DossierMedical;
import com.cabinet.patientservice.model.Patient;
import com.cabinet.patientservice.repository.DossierMedicalRepository;
import com.cabinet.patientservice.repository.PatientRepository;
import com.cabinet.patientservice.service.DossierMedicalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DossierMedicalServiceImpl implements DossierMedicalService {

    private final DossierMedicalRepository dossierMedicalRepository;
    private final PatientRepository patientRepository;

    public DossierMedicalServiceImpl(DossierMedicalRepository dossierMedicalRepository,
            PatientRepository patientRepository) {
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DossierMedicalDTO getDossierByPatientId(Long patientId) {
        DossierMedical dossier = dossierMedicalRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dossier medical for patient id " + patientId + " not found"));
        return DossierMedicalMapper.toDto(dossier);
    }

    @Override
    public DossierMedicalDTO updateDossierByPatientId(Long patientId, DossierMedicalDTO dto) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + patientId + " not found"));

        DossierMedical dossier = dossierMedicalRepository.findByPatient(patient)
                .orElse(new DossierMedical());

        if (dossier.getPatient() == null) {
            dossier.setPatient(patient); // Link new dossier to patient
        }

        DossierMedicalMapper.updateEntityFromDto(dto, dossier);
        DossierMedical saved = dossierMedicalRepository.save(dossier);
        return DossierMedicalMapper.toDto(saved);
    }
}
