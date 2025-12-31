package com.cabinet.consultationservice.service;

import com.cabinet.consultationservice.dto.MedicamentRequestDTO;
import com.cabinet.consultationservice.dto.MedicamentResponseDTO;

import java.util.List;

public interface MedicamentService {

    MedicamentResponseDTO createMedicament(MedicamentRequestDTO requestDTO);

    List<MedicamentResponseDTO> searchMedicamentsByName(String nom);

    void disableMedicament(Long id);
}

