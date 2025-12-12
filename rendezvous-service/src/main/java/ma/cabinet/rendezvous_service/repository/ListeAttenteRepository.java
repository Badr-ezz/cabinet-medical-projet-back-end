package ma.cabinet.rendezvous_service.repository;

import ma.cabinet.rendezvous_service.entity.ListeAttente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListeAttenteRepository extends JpaRepository<ListeAttente,Long> {

    // Vérifier si un rendez-vous est déjà dans la liste d’attente
    Optional<ListeAttente> findByRendezVousId(Long rendezVousId);

    // Compter le nombre d’entrées pour un médecin à une date donnée
    int countByCabinetIdAndMedecinIdAndDateRdv(Long cabinetId, Long medecinId, LocalDate dateRdv);

    List<ListeAttente> findAllByCabinetIdAndMedecinIdAndDateRdvOrderByPositionAsc(
            Long cabinetId, Long medecinId, LocalDate dateRdv);

}
