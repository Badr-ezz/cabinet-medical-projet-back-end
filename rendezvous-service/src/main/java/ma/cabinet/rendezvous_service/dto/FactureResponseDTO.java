package ma.cabinet.rendezvous_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.ModePaiement;
import ma.cabinet.rendezvous_service.enums.StatutFacture;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FactureResponseDTO {

    private Long idFacture;
    private Long rendezVousId;
    private Long cabinetId;
    private BigDecimal montant;
    private ModePaiement modePaiement;
    private StatutFacture statut;
    private LocalDateTime dateCreation;
    private LocalDateTime datePaiement;

    // Infos du rendez-vous
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String motif;

    // Infos du patient
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private String patientTel;
}
