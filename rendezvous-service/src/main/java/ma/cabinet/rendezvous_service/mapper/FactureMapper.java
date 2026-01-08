package ma.cabinet.rendezvous_service.mapper;

import com.example.patient.PatientResponseDTO;
import ma.cabinet.rendezvous_service.dto.FactureRequestDTO;
import ma.cabinet.rendezvous_service.dto.FactureResponseDTO;
import ma.cabinet.rendezvous_service.entity.Facture;
import ma.cabinet.rendezvous_service.entity.RendezVous;
import org.springframework.stereotype.Component;

@Component
public class FactureMapper {

    public Facture toEntity(FactureRequestDTO dto, RendezVous rendezVous) {
        return Facture.builder()
                .rendezVous(rendezVous)
                .cabinetId(dto.getCabinetId())
                .montant(dto.getMontant())
                .modePaiement(dto.getModePaiement())
                .build();
    }

    public FactureResponseDTO toResponseDTO(Facture facture, PatientResponseDTO patient) {
        RendezVous rdv = facture.getRendezVous();
        return FactureResponseDTO.builder()
                .idFacture(facture.getIdFacture())
                .rendezVousId(rdv.getIdRendezVous())
                .cabinetId(facture.getCabinetId())
                .montant(facture.getMontant())
                .modePaiement(facture.getModePaiement())
                .statut(facture.getStatut())
                .dateCreation(facture.getDateCreation())
                .datePaiement(facture.getDatePaiement())
                .dateRdv(rdv.getDateRdv())
                .heureRdv(rdv.getHeureRdv())
                .motif(rdv.getMotif())
                .patientId(rdv.getPatientId())
                .patientNom(patient != null ? patient.getNom() : null)
                .patientPrenom(patient != null ? patient.getPrenom() : null)
                .patientTel(patient != null ? patient.getNumTel() : null)
                .build();
    }
}
