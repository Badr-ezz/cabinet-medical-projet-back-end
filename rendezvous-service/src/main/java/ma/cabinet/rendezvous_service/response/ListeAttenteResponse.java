package ma.cabinet.rendezvous_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.StatutAttente;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListeAttenteResponse {
    private Long id;
    private Long rendezVousId;
    private Long medecinId;
    private LocalDate dateRdv;
    private Integer position;
    private StatutAttente statutAttente;
    private LocalDateTime heureArrivee;
    private Long cabinetId;
}
