package com.example.user_service.request;

import com.example.user_service.entity.UserRole;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

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
