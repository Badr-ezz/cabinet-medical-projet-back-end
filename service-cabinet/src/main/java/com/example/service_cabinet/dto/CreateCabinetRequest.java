package com.example.service_cabinet.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation. constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCabinetRequest {

    @NotBlank(message = "Le nom du cabinet est obligatoire")
    private String nom;

    private String logo;

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @Email(message = "L'email doit être valide")
    private String email;


    @NotNull(message = "Le statut actif est obligatoire")
    private Boolean actif = true;


}