package ma.cabinet.rendezvous_service.service;

import com.example.patient.PatientResponseDTO;
import ma.cabinet.rendezvous_service.feign.PatientFeignClient;
import ma.cabinet.rendezvous_service.feign.UserFeignClient;
import ma.cabinet.rendezvous_service.repository.RendezVousRepository;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import org.springframework.stereotype.Component;
import com.example.auth.AuthResponse;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class RdvValidations {

    private final RendezVousRepository rendezVousRepository;
    private final UserFeignClient userFeignClient;
    private final PatientFeignClient patientFeignClient;

    public RdvValidations(RendezVousRepository rendezVousRepository,
                          UserFeignClient userFeignClient, PatientFeignClient patientFeignClient) {
        this.rendezVousRepository = rendezVousRepository;
        this.userFeignClient = userFeignClient;
        this.patientFeignClient = patientFeignClient;
    }

    // ═══════════════════════════════════════════════════════════════
    // 1️⃣ VALIDATION TOKEN (validité uniquement, pas le rôle)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Vérifie si le token est VALIDE (pas expiré)
     * Retourne l'AuthResponse pour récupérer le rôle ensuite
     */
    public AuthResponse validateToken(String authorizationHeader) {
        try {
            // Extraire le token du header "Bearer xxx"
            String token = extractToken(authorizationHeader);

            if (token == null || token.isEmpty()) {
                System.err.println("❌ Token absent ou vide");
                return null;
            }

            // Appeler USER-SERVICE pour valider
            AuthResponse response = userFeignClient.validateToken(token);

            if (response == null) {
                System.err.println("❌ AuthResponse null depuis USER-SERVICE");
                return null;
            }

            // Retourner la réponse (contient tokenExpired + userRole)
            return response;

        } catch (Exception e) {
            System.err.println("❌ Exception validation token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extrait le token du header "Bearer xxx"
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }

        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }

        return authorizationHeader.trim();
    }

    // ═══════════════════════════════════════════════════════════════
    // 2️⃣ VALIDATION DONNÉES RDV (date, heure, horaires, doublon)
    // ═══════════════════════════════════════════════════════════════

    public boolean validateRendezVousRequest(RendezVousRequest request) {
        System.out.println(">>> VALIDATION RDV APPELEE <<<");

        if (request == null) {
            System.err.println("❌ Request null");
            return false;
        }

        LocalDate date = request.getDateRdv();
        LocalTime time = request.getHeureRdv();

        // 1) Date et heure obligatoires
        if (date == null || time == null) {
            System.err.println("❌ Date ou heure null");
            return false;
        }

        // 2) Date pas dans le passé
        if (date.isBefore(LocalDate.now())) {
            System.err.println("❌ Date dans le passé: " + date);
            return false;
        }

        // 3) Horaires d'ouverture : 08:00–12:00 et 14:00–18:00
        LocalTime startMorning = LocalTime.of(8, 0);
        LocalTime endMorning = LocalTime.of(12, 0);
        LocalTime startAfternoon = LocalTime.of(14, 0);
        LocalTime endAfternoon = LocalTime.of(18, 0);

        boolean inMorning = !time.isBefore(startMorning) && !time.isAfter(endMorning);
        boolean inAfternoon = !time.isBefore(startAfternoon) && !time.isAfter(endAfternoon);

        if (!(inMorning || inAfternoon)) {
            System.err.println("❌ Heure hors horaires ouverture: " + time);
            return false;
        }

        // 4) Pas de doublon (même date + heure)
        boolean exists = rendezVousRepository
                .findByDateRdvAndHeureRdv(date, time)
                .isPresent();

        if (exists) {
            System.err.println("❌ Créneau déjà occupé: " + date + " " + time);
            return false;
        }

        System.out.println("✅ Validation RDV OK");
        return true;
    }

    // ═══════════════════════════════════════════════════════════════
    // 3️⃣ VALIDATION CABINET_ID
    // ═══════════════════════════════════════════════════════════════

    public boolean isCabinetIdValid(Long cabinetId) {
        // TODO: Implémenter quand Cabinet-Service sera prêt
        // Pour l'instant, toujours true
        if (cabinetId == null) {
            System.err.println("❌ CabinetId null");
            return false;
        }

        System.out.println("⚠️ Validation Cabinet temporaire (toujours true)");
        return true;

        // Future implémentation :
        // try {
        //     cabinetFeignClient.getCabinet(cabinetId);
        //     return true;
        // } catch (Exception e) {
        //     System.err.println("❌ Cabinet inexistant: " + cabinetId);
        //     return false;
        // }
    }

    // ═══════════════════════════════════════════════════════════════
    // 4️⃣ VALIDATION PATIENT_ID
    // ═══════════════════════════════════════════════════════════════

    public boolean isPatientExists(Long patientId) {

        if (patientId == null) {
            System.err.println("❌ PatientId null");
            return false;
        }

        System.out.println("⚠️ Validation Patient :");

         try {
             PatientResponseDTO patientResponseDTO = patientFeignClient.getPatientById(patientId);
             if(patientResponseDTO.getId() == null) {
                 return false;
             }
             return true;
         } catch (Exception e) {
             System.err.println("❌ Patient inexistant: " + patientId);
             return false;
         }
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTHODE UTILITAIRE (si besoin ailleurs)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Vérifie si un utilisateur existe (pour médecin par exemple)
     */
    public void validateUserExists(Long userId) {
        try {
            userFeignClient.getUser(userId);
        } catch (Exception e) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID " + userId + " n'existe pas.");
        }
    }
}