package com.example.service_cabinet.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation. constraints.NotNull;
import jakarta.validation.constraints.Email;

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

    private String horaires;

    @NotNull(message = "Le statut actif est obligatoire")
    private Boolean actif = true;

    // Constructeurs
    public CreateCabinetRequest() {
    }

    public CreateCabinetRequest(String nom, String specialite, String adresse, String telephone) {
        this.nom = nom;
        this.specialite = specialite;
        this.adresse = adresse;
        this.telephone = telephone;
        this.actif = true;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "CreateCabinetRequest{" +
                "nom='" + nom + '\'' +
                ", specialite='" + specialite + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}