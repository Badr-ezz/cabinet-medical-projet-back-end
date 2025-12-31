package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.feign.FacturationServiceClient;
import com.cabinet.consultationservice.feign.PatientServiceClient;
import com.cabinet.consultationservice.feign.RendezVousServiceClient;
import com.cabinet.consultationservice.feign.UserServiceClient;
import com.cabinet.consultationservice.feign.dto.DossierMedicalDTO;
import com.cabinet.consultationservice.feign.dto.PatientResponseDTO;
import com.cabinet.consultationservice.feign.dto.RendezVousResponseDTO;
import com.cabinet.consultationservice.feign.dto.UserResponseDTO;
import com.cabinet.consultationservice.model.Consultation;
import com.cabinet.consultationservice.repository.ConsultationRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceImplTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private PatientServiceClient patientServiceClient;

    @Mock
    private RendezVousServiceClient rendezVousServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private FacturationServiceClient facturationServiceClient;

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    private ConsultationRequestDTO requestDTO;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        requestDTO = new ConsultationRequestDTO();
        requestDTO.setPatientId(1L);
        requestDTO.setRendezVousId(1L);
        requestDTO.setMedecinId(1L);
        requestDTO.setType("CONSULTATION");
        requestDTO.setDateConsultation(LocalDate.now());
        requestDTO.setExamenClinique("Examen normal");
        requestDTO.setDiagnostic("Grippe");

        consultation = Consultation.builder()
                .id(1L)
                .patientId(1L)
                .rendezVousId(1L)
                .medecinId(1L)
                .type("CONSULTATION")
                .dateConsultation(LocalDate.now())
                .examenClinique("Examen normal")
                .diagnostic("Grippe")
                .build();
    }

    @Test
    void createConsultation_Success() {
        // Given
        PatientResponseDTO patient = new PatientResponseDTO();
        patient.setId(1L);
        patient.setNom("Dupont");
        patient.setPrenom("Jean");

        DossierMedicalDTO dossier = new DossierMedicalDTO();
        dossier.setPatientId(1L);
        dossier.setAllergies("Pénicilline");

        RendezVousResponseDTO rendezVous = RendezVousResponseDTO.builder()
                .idRendezVous(1L)
                .patientId(1L)
                .statut("CONFIRME")
                .dateRdv(LocalDate.now())
                .heureRdv(LocalTime.of(10, 0))
                .build();

        UserResponseDTO user = UserResponseDTO.builder()
                .id(1L)
                .nom("Dr. Smith")
                .role("MEDECIN")
                .build();

        when(patientServiceClient.getPatientById(1L)).thenReturn(patient);
        when(patientServiceClient.getDossierByPatientId(1L)).thenReturn(dossier);
        when(rendezVousServiceClient.getRendezVousById(1L)).thenReturn(rendezVous);
        when(userServiceClient.getUserById(1L)).thenReturn(user);
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);
        doNothing().when(facturationServiceClient).createFacture(any());

        // When
        ConsultationResponseDTO result = consultationService.createConsultation(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals("CONSULTATION", result.getType());
        verify(patientServiceClient, times(1)).getPatientById(1L);
        verify(patientServiceClient, times(1)).getDossierByPatientId(1L);
        verify(rendezVousServiceClient, times(1)).getRendezVousById(1L);
        verify(userServiceClient, times(1)).getUserById(1L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
        verify(facturationServiceClient, times(1)).createFacture(any());
    }

    @Test
    void createConsultation_ThrowsValidationException_WhenPatientIdIsNull() {
        // Given
        requestDTO.setPatientId(null);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            consultationService.createConsultation(requestDTO);
        });
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void createConsultation_ThrowsValidationException_WhenRendezVousIdIsNull() {
        // Given
        requestDTO.setRendezVousId(null);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            consultationService.createConsultation(requestDTO);
        });
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void createConsultation_ThrowsValidationException_WhenPatientNotFound() {
        // Given
        when(patientServiceClient.getPatientById(1L))
                .thenThrow(FeignException.NotFound.class);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            consultationService.createConsultation(requestDTO);
        });
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void createConsultation_ThrowsValidationException_WhenRendezVousNotConfirmed() {
        // Given
        PatientResponseDTO patient = new PatientResponseDTO();
        patient.setId(1L);

        DossierMedicalDTO dossier = new DossierMedicalDTO();
        dossier.setPatientId(1L);

        RendezVousResponseDTO rendezVous = RendezVousResponseDTO.builder()
                .idRendezVous(1L)
                .patientId(1L)
                .statut("ANNULE") // Not confirmed
                .build();

        when(patientServiceClient.getPatientById(1L)).thenReturn(patient);
        when(patientServiceClient.getDossierByPatientId(1L)).thenReturn(dossier);
        when(rendezVousServiceClient.getRendezVousById(1L)).thenReturn(rendezVous);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            consultationService.createConsultation(requestDTO);
        });
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void createConsultation_Success_WhenDossierMedicalNotFound() {
        // Given - dossier médical might not exist yet
        PatientResponseDTO patient = new PatientResponseDTO();
        patient.setId(1L);
        patient.setNom("Dupont");

        RendezVousResponseDTO rendezVous = RendezVousResponseDTO.builder()
                .idRendezVous(1L)
                .patientId(1L)
                .statut("CONFIRME")
                .dateRdv(LocalDate.now())
                .heureRdv(LocalTime.of(10, 0))
                .build();

        UserResponseDTO user = UserResponseDTO.builder()
                .id(1L)
                .nom("Dr. Smith")
                .role("MEDECIN")
                .build();

        when(patientServiceClient.getPatientById(1L)).thenReturn(patient);
        when(patientServiceClient.getDossierByPatientId(1L))
                .thenThrow(FeignException.NotFound.class); // Dossier not found - should continue
        when(rendezVousServiceClient.getRendezVousById(1L)).thenReturn(rendezVous);
        when(userServiceClient.getUserById(1L)).thenReturn(user);
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);
        doNothing().when(facturationServiceClient).createFacture(any());

        // When
        ConsultationResponseDTO result = consultationService.createConsultation(requestDTO);

        // Then - should succeed even without dossier médical
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    void getConsultationById_Success() {
        // Given
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));

        // When
        ConsultationResponseDTO result = consultationService.getConsultationById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(consultationRepository, times(1)).findById(1L);
    }

    @Test
    void getConsultationById_ThrowsResourceNotFoundException_WhenNotFound() {
        // Given
        when(consultationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.getConsultationById(1L);
        });
        verify(consultationRepository, times(1)).findById(1L);
    }

    @Test
    void deleteConsultation_Success() {
        // Given
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        doNothing().when(consultationRepository).delete(consultation);

        // When
        consultationService.deleteConsultation(1L);

        // Then
        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, times(1)).delete(consultation);
    }

    @Test
    void deleteConsultation_ThrowsResourceNotFoundException_WhenNotFound() {
        // Given
        when(consultationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.deleteConsultation(1L);
        });
        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, never()).delete(any(Consultation.class));
    }
}

