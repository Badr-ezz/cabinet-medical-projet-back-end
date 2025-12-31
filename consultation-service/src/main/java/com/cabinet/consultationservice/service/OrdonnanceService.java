package com.cabinet.consultationservice.service;

import com.cabinet.consultationservice.dto.OrdonnanceRequestDTO;
import com.cabinet.consultationservice.dto.OrdonnanceResponseDTO;

import java.util.List;

public interface OrdonnanceService {

    OrdonnanceResponseDTO createOrdonnance(OrdonnanceRequestDTO requestDTO);

    List<OrdonnanceResponseDTO> getOrdonnancesByConsultationId(Long consultationId);
}

