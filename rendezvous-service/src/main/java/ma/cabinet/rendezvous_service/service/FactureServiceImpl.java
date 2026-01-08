package ma.cabinet.rendezvous_service.service;

import com.example.patient.PatientResponseDTO;
import lombok.RequiredArgsConstructor;
import ma.cabinet.rendezvous_service.feign.PatientFeignClient;
import ma.cabinet.rendezvous_service.dto.FactureRequestDTO;
import ma.cabinet.rendezvous_service.dto.FactureResponseDTO;
import ma.cabinet.rendezvous_service.dto.FactureUpdateDTO;
import ma.cabinet.rendezvous_service.entity.Facture;
import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.enums.StatutFacture;
import ma.cabinet.rendezvous_service.mapper.FactureMapper;
import ma.cabinet.rendezvous_service.repository.FactureRepository;
import ma.cabinet.rendezvous_service.repository.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final RendezVousRepository rendezVousRepository;
    private final PatientFeignClient patientFeignClient;
    private final FactureMapper factureMapper;

    @Override
    public FactureResponseDTO createFacture(FactureRequestDTO request) {
        // Vérifier si une facture existe déjà pour ce rendez-vous
        if (factureRepository.existsByRendezVousIdRendezVous(request.getRendezVousId())) {
            throw new RuntimeException("Une facture existe déjà pour ce rendez-vous");
        }

        // Récupérer le rendez-vous
        RendezVous rendezVous = rendezVousRepository.findById(request.getRendezVousId())
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé avec l'ID: " + request.getRendezVousId()));

        // Créer la facture
        Facture facture = factureMapper.toEntity(request, rendezVous);
        facture = factureRepository.save(facture);

        // Récupérer les infos du patient
        PatientResponseDTO patient = getPatientSafely(rendezVous.getPatientId());

        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureResponseDTO> getAllFacturesByCabinet(Long cabinetId) {
        List<Facture> factures = factureRepository.findAllByCabinetIdOrderByDateCreationDesc(cabinetId);
        return factures.stream()
                .map(facture -> {
                    PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
                    return factureMapper.toResponseDTO(facture, patient);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FactureResponseDTO> getFacturesByCabinetAndStatut(Long cabinetId, StatutFacture statut) {
        List<Facture> factures = factureRepository.findAllByCabinetIdAndStatutOrderByDateCreationDesc(cabinetId, statut);
        return factures.stream()
                .map(facture -> {
                    PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
                    return factureMapper.toResponseDTO(facture, patient);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FactureResponseDTO getFactureById(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + factureId));

        PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    @Transactional(readOnly = true)
    public FactureResponseDTO getFactureByRendezVousId(Long rendezVousId) {
        Facture facture = factureRepository.findByRendezVousIdRendezVous(rendezVousId)
                .orElseThrow(() -> new RuntimeException("Aucune facture trouvée pour ce rendez-vous"));

        PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    public FactureResponseDTO updateFacture(Long factureId, FactureUpdateDTO updateDTO) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + factureId));

        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new RuntimeException("Impossible de modifier une facture déjà payée");
        }

        if (updateDTO.getMontant() != null) {
            facture.setMontant(updateDTO.getMontant());
        }
        if (updateDTO.getModePaiement() != null) {
            facture.setModePaiement(updateDTO.getModePaiement());
        }

        facture = factureRepository.save(facture);

        PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    public FactureResponseDTO marquerCommePaye(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + factureId));

        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new RuntimeException("Cette facture est déjà payée");
        }

        if (facture.getStatut() == StatutFacture.ANNULEE) {
            throw new RuntimeException("Impossible de payer une facture annulée");
        }

        facture.setStatut(StatutFacture.PAYEE);
        facture.setDatePaiement(LocalDateTime.now());
        facture = factureRepository.save(facture);

        PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    public FactureResponseDTO annulerFacture(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + factureId));

        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new RuntimeException("Impossible d'annuler une facture déjà payée");
        }

        facture.setStatut(StatutFacture.ANNULEE);
        facture = factureRepository.save(facture);

        PatientResponseDTO patient = getPatientSafely(facture.getRendezVous().getPatientId());
        return factureMapper.toResponseDTO(facture, patient);
    }

    @Override
    public void deleteFacture(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + factureId));

        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new RuntimeException("Impossible de supprimer une facture payée");
        }

        factureRepository.delete(facture);
    }

    private PatientResponseDTO getPatientSafely(Long patientId) {
        try {
            return patientFeignClient.getPatientById(patientId);
        } catch (Exception e) {
            return null;
        }
    }
}
