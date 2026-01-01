package ma.cabinet.rendezvous_service.feign;

import ma.cabinet.rendezvous_service.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.auth.AuthResponse;
import com.example.patient.PatientResponseDTO;



@FeignClient(name = "patient-service",configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface PatientFeignClient {

    @GetMapping("/api/patients/{id}")
    PatientResponseDTO getPatientById(@PathVariable Long id);
}
