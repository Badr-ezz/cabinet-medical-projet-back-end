package com.cabinet.patientservice.repository;

import com.cabinet.patientservice.model.DossierMedical;
import com.cabinet.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    Optional<DossierMedical> findByPatient(Patient patient);

    Optional<DossierMedical> findByPatientId(Long patientId);
}


