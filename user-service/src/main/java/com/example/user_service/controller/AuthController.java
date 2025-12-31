package com.example.user_service.controller;

import com.example.user_service.request.UserRequest;
import com.example.user_service.response.AuthResponse;
import com.example.user.UserResponse;
import com.example.user_service.service.loginServices.LoginServices;
import com.example.user_service.service.logoutServices.LogoutServices;
import com.example.user_service.service.userServices.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginServices loginServices;
    private final LogoutServices logoutServices;

    @Autowired
    public AuthController(LoginServices loginServices, LogoutServices logoutServices) {
        this.loginServices = loginServices;
        this.logoutServices = logoutServices;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest userReq) {
        try {
            String token = loginServices.authenticate(userReq.getLogin(), userReq.getPwd());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            // see how to handle the exception
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userReq) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(loginServices.register(userReq));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam("token") String token) {
            AuthResponse response = AuthResponse.builder().build();
        try {
            log.info("Validating token:{}", token);
            response = loginServices.checkTokenValidity(token);
            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException e) {
            response.setTokenExpired(true);
            response.setToken(token);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    // extract roles and check the token validity
    //
}
