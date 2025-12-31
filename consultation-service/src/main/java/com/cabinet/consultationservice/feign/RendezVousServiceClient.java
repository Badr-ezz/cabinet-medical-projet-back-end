package com.cabinet.consultationservice.feign;

import com.cabinet.consultationservice.config.FeignClientConfig;
import com.cabinet.consultationservice.feign.dto.RendezVousResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "rendezvous-service", configuration = FeignClientConfig.class)
public interface RendezVousServiceClient {

    @GetMapping("/api/rendezvous/{id}")
    RendezVousResponseDTO getRendezVousById(@PathVariable Long id);
}

