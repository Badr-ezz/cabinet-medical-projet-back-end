package com.cabinet.consultationservice.feign;

import com.cabinet.consultationservice.config.FeignClientConfig;
import com.cabinet.consultationservice.feign.dto.FactureRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "facturation-service", configuration = FeignClientConfig.class)
public interface FacturationServiceClient {

    @PostMapping("/api/factures")
    void createFacture(@RequestBody FactureRequestDTO factureRequest);
}

