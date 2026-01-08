package com.cabinet.consultationservice.service;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;

import java.util.List;

public interface ConsultationService {

    ConsultationResponseDTO createConsultation(ConsultationRequestDTO requestDTO);

    ConsultationResponseDTO getConsultationById(Long id);

    List<ConsultationResponseDTO> getConsultationsByPatientId(Long patientId);

    void deleteConsultation(Long id);
}

