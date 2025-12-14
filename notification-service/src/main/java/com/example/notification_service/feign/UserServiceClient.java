package com.example.notification_service.feign;

import com.example.notification_service.config.FeignClientConfig;
import com.example.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUser(@PathVariable long id);

}
