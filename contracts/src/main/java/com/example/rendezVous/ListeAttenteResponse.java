package com.example.rendezVous;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListeAttenteResponse {
    private Long idRendezVous;
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String motif;
    private String statut;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private String patientTel;
}
