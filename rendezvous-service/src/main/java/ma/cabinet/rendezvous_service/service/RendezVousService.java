package ma.cabinet.rendezvous_service.service;

import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import ma.cabinet.rendezvous_service.response.RendezVousResponse;

import java.time.LocalDate;
import java.util.List;

public interface RendezVousService {
    RendezVousResponse createRendezVous(RendezVousRequest request);

    RendezVousResponse updateRendezVous(Long id, RendezVousRequest request);

    void deleteRendezVous(Long id);

    RendezVousResponse getRendezVousById(Long id);

    List<RendezVousResponse> getAllRendezVous();

    List<RendezVousResponse> getRendezVousByDate(java.time.LocalDate date);

    List<RendezVousResponse> getRendezVousByMedecinAndDate(Long medecinId, LocalDate date);

    RendezVousResponse confirmerRendezVous(Long id);

    RendezVousResponse annulerRendezVous(Long id);

    List<RendezVousResponse> getRendezVousByPatient(Long patientId);

    List<RendezVousResponse> getAllByCabinet(Long cabinetId);

    List<RendezVousResponse> getByMedecinAndDate(Long cabinetId, Long medecinId, LocalDate date);

    List<RendezVousResponse> getHistoriquePatient(Long cabinetId, Long patientId);


}
