package ma.cabinet.rendezvous_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.ModePaiement;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FactureRequestDTO {

    @NotNull(message = "L'ID du rendez-vous est obligatoire")
    private Long rendezVousId;

    @NotNull(message = "L'ID du cabinet est obligatoire")
    private Long cabinetId;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;
}
