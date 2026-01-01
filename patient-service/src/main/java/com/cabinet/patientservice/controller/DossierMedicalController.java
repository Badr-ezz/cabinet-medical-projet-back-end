package com.cabinet.patientservice.controller;

import com.cabinet.patientservice.dto.DossierMedicalDTO;
import com.cabinet.patientservice.service.DossierMedicalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dossiers")
public class DossierMedicalController {

    private final DossierMedicalService dossierMedicalService;

    public DossierMedicalController(DossierMedicalService dossierMedicalService) {
        this.dossierMedicalService = dossierMedicalService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DossierMedicalDTO> getDossierByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(dossierMedicalService.getDossierByPatientId(patientId));
    }

    @PutMapping("/patient/{patientId}")
    public ResponseEntity<DossierMedicalDTO> updateDossierByPatientId(@PathVariable Long patientId,
                                                                      @RequestBody DossierMedicalDTO dto) {
        return ResponseEntity.ok(dossierMedicalService.updateDossierByPatientId(patientId, dto));
    }
}



