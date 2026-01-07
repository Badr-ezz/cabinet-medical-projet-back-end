package com.cabinet.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_patient_cin", columnNames = "cin")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cabinetId;

    @Column(nullable = false, unique = true, length = 50)
    private String cin;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    private LocalDate dateNaissance;

    @Column(length = 10)
    private String sexe;

    @Column(length = 20)
    private String numTel;

    @Column(length = 100)
    private String typeMutuelle;

    @OneToOne(mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private DossierMedical dossierMedical;
}


