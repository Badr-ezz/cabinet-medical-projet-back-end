package ma.cabinet.rendezvous_service.repository;

import jdk.jfr.Registered;
import ma.cabinet.rendezvous_service.entity.ListeAttente;
import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.enums.StatutRDV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


@Registered
public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {
    List<RendezVous> findAllByDateRdv(java.time.LocalDate dateRdv);
    Optional<RendezVous> findByDateRdvAndHeureRdv(LocalDate dateRdv, LocalTime heureRdv);
    List<RendezVous> findAllByMedecinIdAndDateRdv(Long medecinId, LocalDate dateRdv);
    List<RendezVous> findAllByPatientIdOrderByDateRdvAscHeureRdvAsc(Long patientId);

    List<RendezVous> findAllByCabinetId(Long cabinetId);
    List<RendezVous> findAllByCabinetIdAndMedecinIdAndDateRdvOrderByHeureRdvAsc(
            Long cabinetId, Long medecinId, LocalDate dateRdv
    );
    List<RendezVous> findAllByCabinetIdAndPatientIdOrderByDateRdvDescHeureRdvDesc(
            Long cabinetId, Long patientId
    );

    Optional<RendezVous> findByDateRdvAndHeureRdvAndStatutRDVNot(
            LocalDate date,
            LocalTime heure,
            StatutRDV status
    );

}
