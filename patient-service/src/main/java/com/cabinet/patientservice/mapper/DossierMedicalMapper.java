package com.cabinet.patientservice.mapper;

import com.cabinet.patientservice.dto.DossierMedicalDTO;
import com.cabinet.patientservice.model.DossierMedical;
import com.cabinet.patientservice.model.Patient;

public class DossierMedicalMapper {

    private DossierMedicalMapper() {
    }

    public static DossierMedicalDTO toDto(DossierMedical dossierMedical) {
        if (dossierMedical == null) {
            return null;
        }
        DossierMedicalDTO dto = new DossierMedicalDTO();
        dto.setIdDossier(dossierMedical.getIdDossier());
        dto.setAntecedentsMedicaux(dossierMedical.getAntecedentsMedicaux());
        dto.setAntecedentsChirurgicaux(dossierMedical.getAntecedentsChirurgicaux());
        dto.setAllergies(dossierMedical.getAllergies());
        dto.setTraitements(dossierMedical.getTraitements());
        dto.setHabitudes(dossierMedical.getHabitudes());
        dto.setDocumentsMedicaux(dossierMedical.getDocumentsMedicaux());
        dto.setDateCreation(dossierMedical.getDateCreation());
        if (dossierMedical.getPatient() != null) {
            dto.setPatientId(dossierMedical.getPatient().getId());
        }
        return dto;
    }

    public static DossierMedical toEntity(DossierMedicalDTO dto, Patient patient) {
        if (dto == null) {
            return null;
        }
        DossierMedical dossier = new DossierMedical();
        dossier.setIdDossier(dto.getIdDossier());
        dossier.setAntecedentsMedicaux(dto.getAntecedentsMedicaux());
        dossier.setAntecedentsChirurgicaux(dto.getAntecedentsChirurgicaux());
        dossier.setAllergies(dto.getAllergies());
        dossier.setTraitements(dto.getTraitements());
        dossier.setHabitudes(dto.getHabitudes());
        dossier.setDocumentsMedicaux(dto.getDocumentsMedicaux());
        dossier.setDateCreation(dto.getDateCreation());
        dossier.setPatient(patient);
        return dossier;
    }

    public static void updateEntityFromDto(DossierMedicalDTO dto, DossierMedical dossier) {
        if (dto == null || dossier == null) {
            return;
        }
        dossier.setAntecedentsMedicaux(dto.getAntecedentsMedicaux());
        dossier.setAntecedentsChirurgicaux(dto.getAntecedentsChirurgicaux());
        dossier.setAllergies(dto.getAllergies());
        dossier.setTraitements(dto.getTraitements());
        dossier.setHabitudes(dto.getHabitudes());
        dossier.setDocumentsMedicaux(dto.getDocumentsMedicaux());
        // dateCreation and patient are not updated here to preserve original creation info and association
    }
}


