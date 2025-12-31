package com.cabinet.consultationservice.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String login;
    private String nom;
    private String prenom;
    private String signature;
    private String numTel;
    private String role;
}

