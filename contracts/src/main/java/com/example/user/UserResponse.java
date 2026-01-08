package com.example.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private Long cabinetId;
    private String nomCabinet;
    private String login;
    private String nom;
    private String prenom;
    private String signature;
    private String numTel;
    private String role;
}
