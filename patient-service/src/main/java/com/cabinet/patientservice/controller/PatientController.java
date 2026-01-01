package com.cabinet.patientservice.controller;

import com.cabinet.patientservice.dto.PatientRequestDTO;
import com.cabinet.patientservice.service.PatientService;
import com.example.patient.PatientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO requestDTO) {
        PatientResponseDTO response = patientService.createPatient(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<PatientResponseDTO> getPatientByCin(@PathVariable String cin) {
        return ResponseEntity.ok(patientService.getPatientByCin(cin));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDTO>> searchPatientsByNom(@RequestParam("nom") String nom) {
        return ResponseEntity.ok(patientService.searchPatientsByNom(nom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id,
                                                            @Valid @RequestBody PatientRequestDTO requestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}


