package ma.cabinet.rendezvous_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.StatutRDV;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rendez_vous")
@Builder
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRendezVous;

    private LocalDate dateRdv;

    private LocalTime heureRdv;

    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutRDV statut;

    private String notes;

    private Long medecinId;

    private Long patientId;

    private Long cabinetId;

    // Colonnes d'identifiants vers les autres microservices (pas de relation forte)
//    private Long patientId;   // id du patient dans PATIENT-SERVICE
//    private Long medecinId;   // id du médecin dans AUTH-SERVICE
//    private Long cabinetId;   // id du cabinet dans CABINET-SERVICE
//    private Long factureId;   // (optionnel) id de la facture dans FACTURATION-SERVICE

    // Getters / setters / constructeurs (ou Lombok)
}

