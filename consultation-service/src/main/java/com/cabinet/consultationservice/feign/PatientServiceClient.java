package com.cabinet.consultationservice.feign;

import com.cabinet.consultationservice.config.FeignClientConfig;
import com.cabinet.consultationservice.feign.dto.DossierMedicalDTO;
import com.cabinet.consultationservice.feign.dto.PatientResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", configuration = FeignClientConfig.class)
public interface PatientServiceClient {

    @GetMapping("/api/patients/{id}")
    PatientResponseDTO getPatientById(@PathVariable Long id);

    @GetMapping("/api/dossiers/patient/{patientId}")
    DossierMedicalDTO getDossierByPatientId(@PathVariable Long patientId);
}

