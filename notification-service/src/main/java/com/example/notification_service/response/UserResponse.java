package com.example.notification_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String login;
    private String pwd;
    private String nom;
    private String prenom;

    // imagePath vers la signature dl user
    private String signature;
    private String numTel;
    private String role;
}
