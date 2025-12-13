package com.example.user_service.response;


import com.example.user_service.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;

    // Utiliser "tokenExpired" au lieu de "isTokenExpired"
    // pour éviter les problèmes de sérialisation Jackson
    @JsonProperty("tokenExpired")
    private boolean tokenExpired;

    private String error;
    private String userRole;
}
