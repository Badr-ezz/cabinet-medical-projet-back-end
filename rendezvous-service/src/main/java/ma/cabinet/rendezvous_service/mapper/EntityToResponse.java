package ma.cabinet.rendezvous_service.mapper;


import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.response.RendezVousResponse;
import org.springframework.stereotype.Component;

@Component
public class EntityToResponse {



    public RendezVousResponse toResponse(RendezVous entity) {
        RendezVousResponse response = new RendezVousResponse();
        response.setIdRendezVous(entity.getIdRendezVous());
        response.setDateRdv(entity.getDateRdv());
        response.setHeureRdv(entity.getHeureRdv());
        response.setMotif(entity.getMotif());
        response.setStatut(entity.getStatut());
        response.setNotes(entity.getNotes());
        response.setMedecinId(entity.getMedecinId());
        response.setPatientId(entity.getPatientId());
        response.setCabinetId(entity.getCabinetId());
        return response;
    }
}
