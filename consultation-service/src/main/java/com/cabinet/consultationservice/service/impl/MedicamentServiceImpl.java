package com.cabinet.consultationservice.service.impl;

import com.cabinet.consultationservice.dto.MedicamentRequestDTO;
import com.cabinet.consultationservice.dto.MedicamentResponseDTO;
import com.cabinet.consultationservice.exception.ResourceNotFoundException;
import com.cabinet.consultationservice.exception.ValidationException;
import com.cabinet.consultationservice.mapper.MedicamentMapper;
import com.cabinet.consultationservice.model.Medicament;
import com.cabinet.consultationservice.model.Ordonnance;
import com.cabinet.consultationservice.repository.MedicamentRepository;
import com.cabinet.consultationservice.repository.OrdonnanceRepository;
import com.cabinet.consultationservice.service.MedicamentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final OrdonnanceRepository ordonnanceRepository;

    public MedicamentServiceImpl(MedicamentRepository medicamentRepository,
                                 OrdonnanceRepository ordonnanceRepository) {
        this.medicamentRepository = medicamentRepository;
        this.ordonnanceRepository = ordonnanceRepository;
    }

    @Override
    public MedicamentResponseDTO createMedicament(MedicamentRequestDTO requestDTO) {
        if (requestDTO.getOrdonnanceId() == null) {
            throw new ValidationException("Ordonnance ID is required");
        }

        Ordonnance ordonnance = ordonnanceRepository.findById(requestDTO.getOrdonnanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance with id " + requestDTO.getOrdonnanceId() + " not found"));

        Medicament medicament = MedicamentMapper.toEntity(requestDTO);
        medicament.setOrdonnance(ordonnance);
        Medicament saved = medicamentRepository.save(medicament);
        return MedicamentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> searchMedicamentsByName(String nom) {
        return medicamentRepository.findByNomContainingIgnoreCaseAndActifTrue(nom).stream()
                .map(MedicamentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void disableMedicament(Long id) {
        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament with id " + id + " not found"));
        medicament.setActif(false);
        medicamentRepository.save(medicament);
    }
}

