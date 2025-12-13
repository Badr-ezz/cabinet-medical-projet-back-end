package ma.cabinet.rendezvous_service.feign;

import ma.cabinet.rendezvous_service.response.AuthResponse;
import ma.cabinet.rendezvous_service.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface UserFeignClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUser(@PathVariable Long id);

    @GetMapping("/api/auth/validate-token")
    AuthResponse validateToken(@RequestParam("token") String token);
}
