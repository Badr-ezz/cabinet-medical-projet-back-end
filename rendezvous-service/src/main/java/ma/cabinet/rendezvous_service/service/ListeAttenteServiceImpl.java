package ma.cabinet.rendezvous_service.service;


import ma.cabinet.rendezvous_service.entity.ListeAttente;
import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.enums.StatutAttente;
import ma.cabinet.rendezvous_service.enums.StatutRDV;
import ma.cabinet.rendezvous_service.repository.ListeAttenteRepository;
import ma.cabinet.rendezvous_service.repository.RendezVousRepository;
import ma.cabinet.rendezvous_service.response.ListeAttenteResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;


@Service
public class ListeAttenteServiceImpl implements  ListeAttenteService {

    private final ListeAttenteRepository listeAttenteRepository;
    private final RendezVousRepository rendezVousRepository;

    public ListeAttenteServiceImpl(ListeAttenteRepository listeAttenteRepository,
                                   RendezVousRepository rendezVousRepository) {
        this.listeAttenteRepository = listeAttenteRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    @Override
    public ListeAttenteResponse ajouterAListeAttente(Long rendezVousId) {

        // 1) Vérifier que le RDV existe
        RendezVous rdv = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec id=" + rendezVousId));

        // 2) Vérifier qu'il n'est pas déjà dans la liste d'attente
        boolean existe = listeAttenteRepository.findByRendezVousId(rendezVousId).isPresent();
        if (existe) {
            throw new IllegalStateException("Ce rendez-vous est déjà dans la liste d'attente.");
        }

        // 3) Calculer la prochaine position pour ce médecin et cette date
        Long medecinId = rdv.getMedecinId();
        LocalDate dateRdv = rdv.getDateRdv();
        Long cabinetId = rdv.getCabinetId();

        int count = listeAttenteRepository
                .countByCabinetIdAndMedecinIdAndDateRdv(cabinetId, medecinId, dateRdv);
        int nextPosition = count + 1;

        // 4) Créer l'entrée dans la liste d'attente
        ListeAttente entry = new ListeAttente();
        entry.setRendezVousId(rendezVousId);
        entry.setMedecinId(medecinId);
        entry.setDateRdv(dateRdv);
        entry.setPosition(nextPosition);
        entry.setCabinetId(cabinetId);
        entry.setStatutAttente(StatutAttente.EN_ATTENTE);
        entry.setHeureArrivee(LocalDateTime.now());

        ListeAttente saved = listeAttenteRepository.save(entry);

        // 5) Mapper vers la réponse
        ListeAttenteResponse response = new ListeAttenteResponse();
        response.setId(saved.getId());
        response.setRendezVousId(saved.getRendezVousId());
        response.setMedecinId(saved.getMedecinId());
        response.setDateRdv(saved.getDateRdv());
        response.setPosition(saved.getPosition());
        response.setCabinetId(saved.getCabinetId());
        response.setStatutAttente(saved.getStatutAttente());
        response.setHeureArrivee(saved.getHeureArrivee());

        return response;
    }


    @Override
    public List<ListeAttenteResponse> getListeAttentePourMedecinEtDate(Long cabinetId, Long medecinId, LocalDate date) {
        return listeAttenteRepository
                .findAllByCabinetIdAndMedecinIdAndDateRdvOrderByPositionAsc(cabinetId, medecinId, date)
                .stream()
                .map(entry -> {
                    ListeAttenteResponse resp = new ListeAttenteResponse();
                    resp.setId(entry.getId());
                    resp.setRendezVousId(entry.getRendezVousId());
                    resp.setMedecinId(entry.getMedecinId());
                    resp.setCabinetId(entry.getCabinetId());
                    resp.setDateRdv(entry.getDateRdv());
                    resp.setPosition(entry.getPosition());
                    resp.setStatutAttente(entry.getStatutAttente());
                    resp.setHeureArrivee(entry.getHeureArrivee());
                    return resp;
                })
                .toList();
    }

    @Override
    public ListeAttenteResponse envoyerAuMedecin(Long listeAttenteId) {

        // 1) Récupérer l'entrée de liste d'attente
        ListeAttente entry = listeAttenteRepository.findById(listeAttenteId)
                .orElseThrow(() -> new RuntimeException("Entrée de liste d'attente introuvable avec id=" + listeAttenteId));

        // 2) Vérifier que l'entrée est bien en EN_ATTENTE
        if (entry.getStatutAttente() != StatutAttente.EN_ATTENTE) {
            throw new IllegalStateException("Seules les entrées EN_ATTENTE peuvent être envoyées au médecin.");
        }

        // 3) Récupérer le rendez-vous associé
        RendezVous rdv = rendezVousRepository.findById(entry.getRendezVousId())
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable pour cette entrée de liste d'attente."));

        // 4) Mettre à jour les statuts
        entry.setStatutAttente(StatutAttente.EN_COURS);
        rdv.setStatut(StatutRDV.EN_COURS);

        // 5) Sauvegarder
        listeAttenteRepository.save(entry);
        rendezVousRepository.save(rdv);

        // 6) Mapper vers la réponse
        ListeAttenteResponse resp = new ListeAttenteResponse();
        resp.setId(entry.getId());
        resp.setRendezVousId(entry.getRendezVousId());
        resp.setMedecinId(entry.getMedecinId());
        resp.setDateRdv(entry.getDateRdv());
        resp.setPosition(entry.getPosition());
        resp.setStatutAttente(entry.getStatutAttente());
        resp.setHeureArrivee(entry.getHeureArrivee());

        return resp;
    }
}
