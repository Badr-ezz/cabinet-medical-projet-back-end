package ma.cabinet.rendezvous_service.response;


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
@Builder
public class RendezVousResponse {
    private Long idRendezVous;
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String motif;
    private StatutRDV statut;
    private String notes;
    private Long medecinId;
    private Long patientId;
    private Long cabinetId;
}
