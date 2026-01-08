package com.example.patient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponseDTO {

    private Long id;
    private Long cabinetId;
    private String cin;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String numTel;
    private String typeMutuelle;
}

