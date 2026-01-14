package com.cabinet.consultationservice.repository;

import com.cabinet.consultationservice.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByPatientId(Long patientId);

    List<Consultation> findByRendezVousId(Long rendezVousId);

    List<Consultation> findByMedecinId(Long medecinId);
}
