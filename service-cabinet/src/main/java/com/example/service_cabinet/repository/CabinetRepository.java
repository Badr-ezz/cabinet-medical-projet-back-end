package com.example.service_cabinet.repository;




import com.example.service_cabinet.entity.Cabinet;
import org.springframework.data.jpa.repository. JpaRepository;
import org. springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {

    Optional<Cabinet> findByNom(String nom);

    List<Cabinet> findByActifTrue();

    List<Cabinet> findBySpecialite(String specialite);

    boolean existsByNom(String nom);

    List<Cabinet> findByNomContainingIgnoreCase(String nom);
}
