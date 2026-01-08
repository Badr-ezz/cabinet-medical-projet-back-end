package ma.cabinet.rendezvous_service.repository;

import ma.cabinet.rendezvous_service.entity.Facture;
import ma.cabinet.rendezvous_service.enums.StatutFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findAllByCabinetIdOrderByDateCreationDesc(Long cabinetId);

    List<Facture> findAllByCabinetIdAndStatutOrderByDateCreationDesc(Long cabinetId, StatutFacture statut);

    Optional<Facture> findByRendezVousIdRendezVous(Long rendezVousId);

    boolean existsByRendezVousIdRendezVous(Long rendezVousId);
}
