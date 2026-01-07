package com.example.user_service.feign;


import com.example.cabinet.CabinetDTO;
import com.example.user_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "service-cabinet", configuration = FeignClientConfig.class)
public interface CabinetServiceClient {
    @GetMapping("/api/cabinets")
    List<CabinetDTO> getAllCabinets();
}
