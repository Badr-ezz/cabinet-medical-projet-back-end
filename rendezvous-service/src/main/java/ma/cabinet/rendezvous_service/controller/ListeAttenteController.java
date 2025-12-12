package ma.cabinet.rendezvous_service.controller;


import ma.cabinet.rendezvous_service.response.ListeAttenteResponse;
import ma.cabinet.rendezvous_service.service.ListeAttenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/liste-attente")
public class ListeAttenteController {

    private final ListeAttenteService listeAttenteService;

    public ListeAttenteController(ListeAttenteService listeAttenteService) {
        this.listeAttenteService = listeAttenteService;
    }

    // Quand un patient ARRIVE au cabinet
    @PostMapping("/{rendezVousId}")
    public ResponseEntity<ListeAttenteResponse> ajouterAListeAttente(
            @PathVariable Long rendezVousId) {

        ListeAttenteResponse response = listeAttenteService.ajouterAListeAttente(rendezVousId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/medecin/{medecinId}/today")
    public ResponseEntity<List<ListeAttenteResponse>> getListeAttenteMedecinAujourdHui(
            @PathVariable Long medecinId,
            @RequestParam("cabinetId") Long cabinetId) {

        LocalDate today = LocalDate.now();
        List<ListeAttenteResponse> list =
                listeAttenteService.getListeAttentePourMedecinEtDate(cabinetId, medecinId, today);

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/envoyer-au-medecin")
    public ResponseEntity<ListeAttenteResponse> envoyerAuMedecin(
            @PathVariable Long id) {

        ListeAttenteResponse response = listeAttenteService.envoyerAuMedecin(id);
        return ResponseEntity.ok(response);
    }
}
