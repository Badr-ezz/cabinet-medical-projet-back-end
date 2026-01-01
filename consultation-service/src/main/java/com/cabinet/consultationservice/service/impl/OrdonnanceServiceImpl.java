package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.OrdonnanceRequestDTO;
import com.cabinet.consultationservice.dto.OrdonnanceResponseDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.mapper.OrdonnanceMapper;
import com.cabinet.consultationservice.model.Consultation;
import com.cabinet.consultationservice.model.Ordonnance;
import com.cabinet.consultationservice.repository.ConsultationRepository;
import com.cabinet.consultationservice.repository.OrdonnanceRepository;
import com.cabinet.consultationservice.service.OrdonnanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepository;
    private final ConsultationRepository consultationRepository;

    public OrdonnanceServiceImpl(OrdonnanceRepository ordonnanceRepository,
                                 ConsultationRepository consultationRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.consultationRepository = consultationRepository;
    }

    @Override
    public OrdonnanceResponseDTO createOrdonnance(OrdonnanceRequestDTO requestDTO) {
        if (requestDTO.getConsultationId() == null) {
            throw new ValidationException("Consultation ID is required");
        }

        Consultation consultation = consultationRepository.findById(requestDTO.getConsultationId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + requestDTO.getConsultationId() + " not found"));

        Ordonnance ordonnance = OrdonnanceMapper.toEntity(requestDTO);
        ordonnance.setConsultation(consultation);
        Ordonnance saved = ordonnanceRepository.save(ordonnance);
        return OrdonnanceMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdonnanceResponseDTO> getOrdonnancesByConsultationId(Long consultationId) {
        return ordonnanceRepository.findByConsultationId(consultationId).stream()
                .map(OrdonnanceMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}

