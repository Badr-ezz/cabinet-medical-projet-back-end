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
        if (requestDTO.getRendezVousId() == null) {
            throw new ValidationException("Rendez-vous ID is required");
        }
        if (requestDTO.getMedecinId() == null) {
            throw new ValidationException("Medecin ID is required");
        }

        // 1. Fetch and validate patient data from patient-service
        PatientResponseDTO patient;
        try {
            patient = patientServiceClient.getPatientById(requestDTO.getPatientId());
        } catch (FeignException.NotFound e) {
            throw new ValidationException("Patient with id " + requestDTO.getPatientId() + " not found in patient-service");
        } catch (FeignException e) {
            throw new ValidationException("Error communicating with patient-service: " + e.getMessage());
        }

        // 2. Fetch patient's medical dossier (dossier médical) for allergies and medical history
        DossierMedicalDTO dossierMedical;
        try {
            dossierMedical = patientServiceClient.getDossierByPatientId(requestDTO.getPatientId());
            log.info("Fetched dossier médical for patient {}: allergies={}", requestDTO.getPatientId(), dossierMedical.getAllergies());
        } catch (FeignException.NotFound e) {
            log.warn("Dossier médical not found for patient {}", requestDTO.getPatientId());
            dossierMedical = null; // Dossier might not exist yet, continue anyway
        } catch (FeignException e) {
            log.warn("Error fetching dossier médical: {}", e.getMessage());
            dossierMedical = null; // Continue without dossier data
        }

        // 3. Validate that rendez-vous exists and is confirmed
        RendezVousResponseDTO rendezVous;
        try {
            rendezVous = rendezVousServiceClient.getRendezVousById(requestDTO.getRendezVousId());
            // Verify rendez-vous is for the correct patient
            if (!rendezVous.getPatientId().equals(requestDTO.getPatientId())) {
                throw new ValidationException("Rendez-vous " + requestDTO.getRendezVousId() + 
                    " does not belong to patient " + requestDTO.getPatientId());
            }
            // Verify rendez-vous is confirmed
            if (!"CONFIRME".equalsIgnoreCase(rendezVous.getStatut())) {
                throw new ValidationException("Rendez-vous " + requestDTO.getRendezVousId() + 
                    " is not confirmed. Current status: " + rendezVous.getStatut());
            }
        } catch (FeignException.NotFound e) {
            throw new ValidationException("Rendez-vous with id " + requestDTO.getRendezVousId() + " not found in rendezvous-service");
        } catch (FeignException e) {
            throw new ValidationException("Error communicating with rendezvous-service: " + e.getMessage());
        }

        // 4. Validate that medecin exists in user-service
        try {
            userServiceClient.getUserById(requestDTO.getMedecinId());
        } catch (FeignException.NotFound e) {
            throw new ValidationException("Medecin with id " + requestDTO.getMedecinId() + " not found in user-service");
        } catch (FeignException e) {
            throw new ValidationException("Error communicating with user-service: " + e.getMessage());
        }

        // 5. Create the consultation
        Consultation consultation = ConsultationMapper.toEntity(requestDTO);
        Consultation saved = consultationRepository.save(consultation);
        
        // 6. Send billing information to facturation-service (optional/non-blocking)
        // If facturation-service is not available, billing can be handled later
        sendBillingToFacturationService(saved.getId(), requestDTO);

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
            log.warn("Facturation-service unavailable. Billing for consultation {} will be handled later", consultationId);
        } catch (Exception e) {
            // Any other error - log but don't fail consultation
            log.error("Error sending billing to facturation-service for consultation {}: {}", consultationId, e.getMessage());
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
                    .map(ordonnance -> com.cabinet.consultationservice.mapper.OrdonnanceMapper.toResponseDto(ordonnance))
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
    public void deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));
        // Ordonnances and Medicaments will be deleted because of cascade and orphanRemoval
        consultationRepository.delete(consultation);
    }
}

