package ma.cabinet.rendezvous_service.mapper;

import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;

public class EntityToRequest {

    private EntityToRequest() {
        // constructeur privé pour empêcher l'instanciation
    }

    public static RendezVous toEntity(RendezVousRequest request) {
        RendezVous rdv = new RendezVous();
        rdv.setDateRdv(request.getDateRdv());
        rdv.setHeureRdv(request.getHeureRdv());
        rdv.setMotif(request.getMotif());
        rdv.setStatut(request.getStatut());
        rdv.setNotes(request.getNotes());
        rdv.setMedecinId(request.getMedecinId());
        rdv.setPatientId(request.getPatientId());
        rdv.setCabinetId(request.getCabinetId());
        return rdv;
    }

    public static void updateEntityFromRequest(RendezVous rdv, RendezVousRequest request) {
        rdv.setDateRdv(request.getDateRdv());
        rdv.setHeureRdv(request.getHeureRdv());
        rdv.setMotif(request.getMotif());
        rdv.setStatut(request.getStatut());
        rdv.setNotes(request.getNotes());
        rdv.setMedecinId(request.getMedecinId());
        rdv.setPatientId(request.getPatientId());
        rdv.setCabinetId(request.getCabinetId());
    }
}
