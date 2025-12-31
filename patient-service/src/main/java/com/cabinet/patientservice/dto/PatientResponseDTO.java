package com.cabinet.patientservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponseDTO {

    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String numTel;
    private String typeMutuelle;
}


