package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.dto.ConsultationWithPatientDataDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.feign.FacturationServiceClient;
import com.cabinet.consultationservice.feign.PatientServiceClient;
import com.cabinet.consultationservice.feign.RendezVousServiceClient;
import com.cabinet.consultationservice.feign.UserServiceClient;
import com.cabinet.consultationservice.feign.dto.DossierMedicalDTO;
import com.cabinet.consultationservice.feign.dto.FactureRequestDTO;
import com.cabinet.consultationservice.feign.dto.PatientResponseDTO;
import com.cabinet.consultationservice.feign.dto.RendezVousResponseDTO;
import com.cabinet.consultationservice.mapper.ConsultationMapper;
import com.cabinet.consultationservice.model.Consultation;
import com.cabinet.consultationservice.repository.ConsultationRepository;
import com.cabinet.consultationservice.service.ConsultationService;
import feign.FeignException;
import com.cabinet.consultationservice.model.Medicament;
import com.cabinet.consultationservice.model.Ordonnance;
import com.cabinet.consultationservice.enums.TypeOrdonnance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientServiceClient patientServiceClient;
    private final RendezVousServiceClient rendezVousServiceClient;
    private final UserServiceClient userServiceClient;
    private final FacturationServiceClient facturationServiceClient;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
            PatientServiceClient patientServiceClient,
            RendezVousServiceClient rendezVousServiceClient,
            UserServiceClient userServiceClient,
            FacturationServiceClient facturationServiceClient) {
        this.consultationRepository = consultationRepository;
        this.patientServiceClient = patientServiceClient;
        this.rendezVousServiceClient = rendezVousServiceClient;
        this.userServiceClient = userServiceClient;
        this.facturationServiceClient = facturationServiceClient;
    }

    @Override
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO requestDTO) {
        if (requestDTO.getPatientId() == null) {
            throw new ValidationException("Patient ID is required");
        }
        // ... (validations)

        // ... (checks for patient, rendezvous, medecin)

        // 5. Create the consultation
        Consultation consultation = ConsultationMapper.toEntity(requestDTO);

        // Handle Medicaments
        if (requestDTO.getMedicaments() != null && !requestDTO.getMedicaments().isEmpty()) {
            log.info("Received {} medicaments for consultation.", requestDTO.getMedicaments().size());
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setType(TypeOrdonnance.MEDICAMENT);
            ordonnance.setConsultation(consultation);

            List<Medicament> medicaments = requestDTO.getMedicaments().stream().map(mDto -> {
                Medicament m = new Medicament();
                m.setNom(mDto.getNom());
                m.setDosage(mDto.getDosage());
                m.setDuree(mDto.getDuree());
                m.setDescription(mDto.getDescription());
                m.setOrdonnance(ordonnance);
                return m;
            }).collect(Collectors.toList());

            ordonnance.setMedicaments(medicaments);
            consultation.getOrdonnances().add(ordonnance);
        }

        Consultation saved = consultationRepository.save(consultation);

        // ...

        return ConsultationMapper.toResponseDto(saved);
    }

    /**
     * Send billing information to facturation-service (non-blocking, optional)
     * If facturation-service is unavailable, this fails silently
     */
    private void sendBillingToFacturationService(Long consultationId, ConsultationRequestDTO requestDTO) {
        try {
            FactureRequestDTO factureRequest = FactureRequestDTO.builder()
                    .consultationId(consultationId)
                    .patientId(requestDTO.getPatientId())
                    .montant(calculateConsultationAmount(requestDTO))
                    .modePaiement("ASSURANCE") // Default - can be made configurable
                    .build();

            facturationServiceClient.createFacture(factureRequest);
            log.info("Billing information sent to facturation-service for consultation {}", consultationId);
        } catch (FeignException.ServiceUnavailable | FeignException.NotFound e) {
            // Service not available - billing can be handled later
            log.warn("Facturation-service unavailable. Billing for consultation {} will be handled later",
                    consultationId);
        } catch (Exception e) {
            // Any other error - log but don't fail consultation
            log.error("Error sending billing to facturation-service for consultation {}: {}", consultationId,
                    e.getMessage());
        }
    }

    /**
     * Calculate consultation amount based on consultation type and other factors
     * This is a placeholder - implement your business logic here
     */
    private Double calculateConsultationAmount(ConsultationRequestDTO requestDTO) {
        // Default amount - implement your pricing logic
        if ("CONSULTATION".equalsIgnoreCase(requestDTO.getType())) {
            return 200.0; // Example: 200 MAD for consultation
        } else if ("CONTROLE".equalsIgnoreCase(requestDTO.getType())) {
            return 100.0; // Example: 100 MAD for controle
        }
        return 150.0; // Default amount
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationResponseDTO getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));
        return ConsultationMapper.toResponseDto(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationWithPatientDataDTO getConsultationWithPatientData(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));

        ConsultationWithPatientDataDTO dto = ConsultationWithPatientDataDTO.builder()
                .id(consultation.getId())
                .patientId(consultation.getPatientId())
                .rendezVousId(consultation.getRendezVousId())
                .medecinId(consultation.getMedecinId())
                .type(consultation.getType())
                .dateConsultation(consultation.getDateConsultation())
                .examenClinique(consultation.getExamenClinique())
                .examenSupplementaire(consultation.getExamenSupplementaire())
                .diagnostic(consultation.getDiagnostic())
                .observations(consultation.getObservations())
                .createdAt(consultation.getCreatedAt())
                .build();

        // Fetch patient data
        try {
            dto.setPatient(patientServiceClient.getPatientById(consultation.getPatientId()));
        } catch (FeignException e) {
            log.warn("Error fetching patient data: {}", e.getMessage());
        }

        // Fetch dossier médical
        try {
            dto.setDossierMedical(patientServiceClient.getDossierByPatientId(consultation.getPatientId()));
        } catch (FeignException e) {
            log.warn("Error fetching dossier médical: {}", e.getMessage());
        }

        // Map ordonnances
        if (consultation.getOrdonnances() != null && !consultation.getOrdonnances().isEmpty()) {
            dto.setOrdonnances(consultation.getOrdonnances().stream()
                    .map(ordonnance -> com.cabinet.consultationservice.mapper.OrdonnanceMapper
                            .toResponseDto(ordonnance))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationResponseDTO> getConsultationsByPatientId(Long patientId) {
        return consultationRepository.findByPatientId(patientId).stream()
                .map(ConsultationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationResponseDTO> getConsultationsByMedecinId(Long medecinId) {
        return consultationRepository.findByMedecinId(medecinId).stream()
                .map(ConsultationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));
        // Ordonnances and Medicaments will be deleted because of cascade and
        // orphanRemoval
        consultationRepository.delete(consultation);
    }
}
