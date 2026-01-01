package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.model.Consultation;
import com.cabinet.consultationservice.repository.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceImplTest {

    @Mock
    private ConsultationRepository consultationRepository;

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
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        // When
        ConsultationResponseDTO result = consultationService.createConsultation(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals("CONSULTATION", result.getType());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
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

