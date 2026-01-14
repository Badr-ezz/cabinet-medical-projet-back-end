package com.cabinet.patientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String cin;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(max = 100)
    private String prenom;

    private LocalDate dateNaissance;

    private String sexe;

    private String numTel;

    private String typeMutuelle;
    private String email;
    private String adresse;
    private Long cabinetId;
}
