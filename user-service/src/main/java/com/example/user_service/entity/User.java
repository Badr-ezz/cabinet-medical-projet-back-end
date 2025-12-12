package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    private String pwd;
    private String nom;
    private String prenom;

    // imagePath vers la signature dl user
    @Column(unique = true)
    private String signature;

    @Column(unique = true)
    private String numTel;
    private UserRole role;


}
