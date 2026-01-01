package com.cabinet.patientservice.repository;

import com.cabinet.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByCin(String cin);

    boolean existsByCin(String cin);

    List<Patient> findByNomIgnoreCaseContaining(String nom);
}


