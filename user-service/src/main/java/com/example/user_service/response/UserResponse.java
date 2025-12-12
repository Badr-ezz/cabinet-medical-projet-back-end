package com.example.user_service.response;

import com.example.user_service.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String login;
    private String pwd;
    private String nom;
    private String prenom;

    // imagePath vers la signature dl user
    private String signature;
    private String numTel;
    private UserRole role;
}
