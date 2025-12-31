package com.cabinet.consultationservice.repository;

import com.cabinet.consultationservice.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findByOrdonnanceId(Long ordonnanceId);

    List<Medicament> findByNomContainingIgnoreCaseAndActifTrue(String nom);
}

