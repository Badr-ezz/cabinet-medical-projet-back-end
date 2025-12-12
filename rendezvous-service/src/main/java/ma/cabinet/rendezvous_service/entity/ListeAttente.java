package ma.cabinet.rendezvous_service.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.StatutAttente;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "liste_attente")
@Builder
public class ListeAttente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lien vers le rendez-vous concerné (table rendez_vous)
    private Long rendezVousId;


    private Long medecinId;

    private LocalDate dateRdv;

    // Position dans la file pour ce médecin / ce jour
    private Integer position;

    @Enumerated(EnumType.STRING)
    private StatutAttente statutAttente;

    // Heure à laquelle le patient a été ajouté à la liste d'attente
    private LocalDateTime heureArrivee;

    private Long cabinetId;

}
