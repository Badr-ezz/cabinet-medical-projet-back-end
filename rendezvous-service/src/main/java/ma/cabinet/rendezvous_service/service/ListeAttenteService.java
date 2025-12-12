package ma.cabinet.rendezvous_service.service;

import ma.cabinet.rendezvous_service.response.ListeAttenteResponse;

import java.time.LocalDate;
import java.util.List;

public interface ListeAttenteService {

    // Quand le patient arrive au cabinet
    ListeAttenteResponse ajouterAListeAttente(Long rendezVousId);

    // liste d’attente d’un médecin pour une date
    List<ListeAttenteResponse> getListeAttentePourMedecinEtDate(Long cabinetId, Long medecinId, LocalDate date);


    ListeAttenteResponse envoyerAuMedecin(Long listeAttenteId);
}
