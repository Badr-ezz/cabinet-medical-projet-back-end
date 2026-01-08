package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.mapper.ConsultationMapper;
import com.cabinet.consultationservice.model.Consultation;
import com.cabinet.consultationservice.repository.ConsultationRepository;
import com.cabinet.consultationservice.service.ConsultationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO requestDTO) {
        if (requestDTO.getPatientId() == null) {
            throw new ValidationException("Patient ID is required");
        }
        if (requestDTO.getRendezVousId() == null) {
            throw new ValidationException("Rendez-vous ID is required");
        }

        Consultation consultation = ConsultationMapper.toEntity(requestDTO);
        Consultation saved = consultationRepository.save(consultation);
        return ConsultationMapper.toResponseDto(saved);
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

