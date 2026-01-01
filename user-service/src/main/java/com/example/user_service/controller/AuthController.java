package com.example.user_service.controller;

import com.example.user_service.request.UserRequest;
import com.example.user_service.response.AuthResponse;
import com.example.user.UserResponse;
import com.example.user_service.service.loginServices.LoginServices;
import com.example.user_service.service.logoutServices.LogoutServices;
import com.example.user_service.service.userServices.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest userReq) {
        AuthResponse authResponse = loginServices.authenticate(userReq.getLogin(), userReq.getPwd());

        if (authResponse.getError() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        return ResponseEntity.ok(authResponse);
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

    // Logout endpoint: extracts bearer token from Authorization header and delegates to LogoutServices
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = header.substring(7);
        logoutServices.logout(token);
        return ResponseEntity.ok().build();
    }

    // extract roles and check the token validity
    //
}
