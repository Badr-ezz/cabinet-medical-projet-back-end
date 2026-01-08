package ma.cabinet.rendezvous_service.service;

import ma.cabinet.rendezvous_service.dto.FactureRequestDTO;
import ma.cabinet.rendezvous_service.dto.FactureResponseDTO;
import ma.cabinet.rendezvous_service.dto.FactureUpdateDTO;
import ma.cabinet.rendezvous_service.enums.StatutFacture;

import java.util.List;

public interface FactureService {

    FactureResponseDTO createFacture(FactureRequestDTO request);

    List<FactureResponseDTO> getAllFacturesByCabinet(Long cabinetId);

    List<FactureResponseDTO> getFacturesByCabinetAndStatut(Long cabinetId, StatutFacture statut);

    FactureResponseDTO getFactureById(Long factureId);

    FactureResponseDTO getFactureByRendezVousId(Long rendezVousId);

    FactureResponseDTO updateFacture(Long factureId, FactureUpdateDTO updateDTO);

    FactureResponseDTO marquerCommePaye(Long factureId);

    FactureResponseDTO annulerFacture(Long factureId);

    void deleteFacture(Long factureId);
}
