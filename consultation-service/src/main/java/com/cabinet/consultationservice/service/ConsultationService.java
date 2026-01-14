package com.cabinet.consultationservice.service;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.dto.ConsultationWithPatientDataDTO;

import java.util.List;

public interface ConsultationService {

    ConsultationResponseDTO createConsultation(ConsultationRequestDTO requestDTO);

    ConsultationResponseDTO getConsultationById(Long id);

    ConsultationWithPatientDataDTO getConsultationWithPatientData(Long id);

    List<ConsultationResponseDTO> getConsultationsByPatientId(Long patientId);

    List<ConsultationResponseDTO> getConsultationsByMedecinId(Long medecinId);

    void deleteConsultation(Long id);
}
