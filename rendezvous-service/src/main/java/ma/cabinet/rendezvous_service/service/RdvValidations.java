package ma.cabinet.rendezvous_service.service;

import ma.cabinet.rendezvous_service.feign.UserFeignClient;
import ma.cabinet.rendezvous_service.repository.RendezVousRepository;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import ma.cabinet.rendezvous_service.response.AuthResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class RdvValidations {


    private final RendezVousRepository rendezVousRepository;
    private final UserFeignClient userFeignClient;


    public RdvValidations(RendezVousRepository rendezVousRepository, UserFeignClient userFeignClient) {
        this.rendezVousRepository = rendezVousRepository;
        this.userFeignClient = userFeignClient;
    }



    public boolean validateRendezVousRequest(RendezVousRequest request) {
        System.out.println(">>> VALIDATION RDV APPELEE <<<");

        if (request == null) return false;

        LocalDate date = request.getDateRdv();
        LocalTime time = request.getHeureRdv();

        // 1) date/heure obligatoires
        if (date == null || time == null) return false;

        // 2) date pas dans le passé
        if (date.isBefore(LocalDate.now())) return false;

        // 3) horaires d'ouverture : 08:00–12:00 et 14:00–18:00
        LocalTime startMorning = LocalTime.of(8, 0);
        LocalTime endMorning = LocalTime.of(12, 0);
        LocalTime startAfternoon = LocalTime.of(14, 0);
        LocalTime endAfternoon = LocalTime.of(18, 0);

        boolean inMorning = !time.isBefore(startMorning) && !time.isAfter(endMorning);
        boolean inAfternoon = !time.isBefore(startAfternoon) && !time.isAfter(endAfternoon);

        if (!(inMorning || inAfternoon)) return false;

        // 4) pas de doublon sur (date + heure)
        boolean exists = rendezVousRepository
                .findByDateRdvAndHeureRdv(date, time)
                .isPresent();

        if (exists) return false;

        System.out.println("LocalDate.now() = " + LocalDate.now());
        System.out.println("dateRdv reçue = " + date);

        return true;
    }


    public void validateUserExists(Long userId) {
        try {
            userFeignClient.getUser(userId);
        } catch (Exception e) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID " + userId + " n'existe pas.");
        }
    }

    public boolean isTokenValid(String token) {
        AuthResponse response = userFeignClient.validateToken(token);
        if(response.isTokenExpired()){
            return false;
        }
        else if(!response.getUserRole().equals("SECRETARY")){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean isCabinetIdValid(Long cabinetId) {
        try {
//            userFeignClient.getCabinet(cabinetId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ispatientExists(Long patientId) {
        try {
//            userFeignClient.getUser(patientId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
