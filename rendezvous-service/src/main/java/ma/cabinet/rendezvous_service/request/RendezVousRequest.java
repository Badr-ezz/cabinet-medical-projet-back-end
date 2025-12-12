package ma.cabinet.rendezvous_service.request;


import jakarta.validation.constraints.NotNull;
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
public class RendezVousRequest {
    @NotNull
    private LocalDate dateRdv;

    @NotNull
    private LocalTime heureRdv;

    @NotNull
    private String motif;

    @NotNull
    private StatutRDV statut;

    private String notes;

    private Long medecinId;

    private Long patientId;

    private Long cabinetId;

}
