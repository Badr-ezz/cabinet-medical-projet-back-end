package ma.cabinet.rendezvous_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.ModePaiement;
import ma.cabinet.rendezvous_service.enums.StatutFacture;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factures")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rendez_vous_id", nullable = false)
    private RendezVous rendezVous;

    @Column(nullable = false)
    private Long cabinetId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime datePaiement;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutFacture.EN_ATTENTE;
        }
    }
}
