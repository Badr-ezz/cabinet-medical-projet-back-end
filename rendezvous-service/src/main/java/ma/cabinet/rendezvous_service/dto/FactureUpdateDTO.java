package ma.cabinet.rendezvous_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cabinet.rendezvous_service.enums.ModePaiement;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactureUpdateDTO {

    private BigDecimal montant;

    private ModePaiement modePaiement;
}
