package com.cabinet.patientservice.service;

import com.cabinet.patientservice.dto.DossierMedicalDTO;

public interface DossierMedicalService {

    DossierMedicalDTO getDossierByPatientId(Long patientId);

    DossierMedicalDTO updateDossierByPatientId(Long patientId, DossierMedicalDTO dto);
}



