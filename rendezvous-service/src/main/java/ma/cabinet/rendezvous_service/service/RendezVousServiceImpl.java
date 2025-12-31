package ma.cabinet.rendezvous_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import ma.cabinet.rendezvous_service.enums.StatutRDV;
import ma.cabinet.rendezvous_service.feign.UserFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.cabinet.rendezvous_service.entity.RendezVous;
import ma.cabinet.rendezvous_service.mapper.EntityToRequest;
import ma.cabinet.rendezvous_service.mapper.EntityToResponse;
import ma.cabinet.rendezvous_service.repository.RendezVousRepository;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import ma.cabinet.rendezvous_service.response.RendezVousResponse;

@Service
@Transactional
public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final EntityToResponse mapper;
    public final UserFeignClient userFeignClient;
    public final RdvValidations rdvValidations;
    private final HttpServletRequest httpRequest;

    public RendezVousServiceImpl(RendezVousRepository rendezVousRepository, EntityToResponse mapper, UserFeignClient userFeignClient, RdvValidations rdvValidations, HttpServletRequest httpRequest) {
        this.rendezVousRepository = rendezVousRepository;
        this.mapper = mapper;
        this.userFeignClient = userFeignClient;
        this.rdvValidations = rdvValidations;
        this.httpRequest = httpRequest;
    }

    @Override
    public RendezVousResponse createRendezVous(RendezVousRequest request) {
        // Validation données RDV
        if (!rdvValidations.validateRendezVousRequest(request)) {
            throw new IllegalArgumentException("Données du rendez-vous invalides");
        }

        // Validation Cabinet
        if (!rdvValidations.isCabinetIdValid(request.getCabinetId())) {
            throw new IllegalArgumentException("Cabinet inexistant: " + request.getCabinetId());
        }

        // Validation Patient
        if (!rdvValidations.isPatientExists(request.getPatientId())) {
            throw new IllegalArgumentException("Patient inexistant: " + request.getPatientId());
        }

        // Création
        RendezVous rdv = EntityToRequest.toEntity(request);
        RendezVous saved = rendezVousRepository.save(rdv);

        return mapper.toResponse(saved);
    }



    @Override
    public RendezVousResponse updateRendezVous(Long id, RendezVousRequest request) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec id=" + id));

        if (!rdvValidations.validateRendezVousRequest(request)) {
            throw new IllegalArgumentException("Données du rendez-vous invalides");
        }

        // Validation Cabinet
        if (!rdvValidations.isCabinetIdValid(request.getCabinetId())) {
            throw new IllegalArgumentException("Cabinet inexistant: " + request.getCabinetId());
        }

        // Validation Patient
        if (!rdvValidations.isPatientExists(request.getPatientId())) {
            throw new IllegalArgumentException("Patient inexistant: " + request.getPatientId());
        }

        EntityToRequest.updateEntityFromRequest(rdv, request);

        RendezVous updated = rendezVousRepository.save(rdv);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteRendezVous(Long id) {
        if (!rendezVousRepository.existsById(id)) {
            throw new RuntimeException("Rendez-vous introuvable avec id=" + id);
        }
        rendezVousRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RendezVousResponse getRendezVousById(Long id) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec id=" + id));

        return mapper.toResponse(rdv);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponse> getAllRendezVous() {
        return rendezVousRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVousResponse> getRendezVousByDate(LocalDate date) {
        return rendezVousRepository.findAllByDateRdv(date)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

//    private void validateRendezVousRequest(RendezVousRequest request) {
//        System.out.println(">>> VALIDATION RDV APPELEE <<<");
//        LocalDate date = request.getDateRdv();
//        LocalTime time = request.getHeureRdv();
//
//        // 1) Vérifier que la date n'est pas dans le passé
//        if (date == null || time == null) {
//            throw new IllegalArgumentException("La date et l'heure du rendez-vous sont obligatoires.");
//        }
//
//        if (date.isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("La date du rendez-vous ne peut pas être dans le passé.");
//        }
//
//        // 2) Vérifier les horaires d'ouverture : 08:00–12:00 et 14:00–18:00
//        LocalTime startMorning = LocalTime.of(8, 0);
//        LocalTime endMorning = LocalTime.of(12, 0);
//        LocalTime startAfternoon = LocalTime.of(14, 0);
//        LocalTime endAfternoon = LocalTime.of(18, 0);
//
//        boolean inMorning = !time.isBefore(startMorning) && !time.isAfter(endMorning);
//        boolean inAfternoon = !time.isBefore(startAfternoon) && !time.isAfter(endAfternoon);
//
//        if (!(inMorning || inAfternoon)) {
//            throw new IllegalArgumentException(
//                    "L'heure du rendez-vous doit être entre 08:00–12:00 ou 14:00–18:00.");
//        }
//
//        // 3) Vérifier qu'il n'y a pas déjà un rendez-vous à ce créneau
//        boolean exists = rendezVousRepository
//                .findByDateRdvAndHeureRdv(date, time)
//                .isPresent();
//
//        if (exists) {
//            throw new IllegalArgumentException(
//                    "Un rendez-vous existe déjà pour ce créneau (date + heure).");
//        }
//
//        System.out.println("LocalDate.now() = " + LocalDate.now());
//        System.out.println("dateRdv reçue = " + request.getDateRdv());
//    }


    @Override
    public List<RendezVousResponse> getRendezVousByMedecinAndDate(Long medecinId, LocalDate date) {
        return rendezVousRepository.findAllByMedecinIdAndDateRdv(medecinId, date)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RendezVousResponse confirmerRendezVous(Long id) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec id=" + id));

        // Règles simples : on évite de confirmer un RDV déjà annulé
        if (rdv.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de confirmer un rendez-vous annulé.");
        }

        // (Optionnel) On peut aussi vérifier la date ici si tu veux :
        // if (rdv.getDateRdv().isBefore(LocalDate.now())) { ... }

        rdv.setStatut(StatutRDV.CONFIRME);
        RendezVous updated = rendezVousRepository.save(rdv);

        return mapper.toResponse(updated);
    }

    @Override
    public RendezVousResponse annulerRendezVous(Long id) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec id=" + id));

        if (rdv.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Le rendez-vous est déjà annulé.");
        }

        rdv.setStatut(StatutRDV.ANNULE);
        RendezVous updated = rendezVousRepository.save(rdv);

        return mapper.toResponse(updated);
    }

    @Override
    public List<RendezVousResponse> getRendezVousByPatient(Long patientId) {
        return rendezVousRepository.findAllByPatientIdOrderByDateRdvAscHeureRdvAsc(patientId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RendezVousResponse> getAllByCabinet(Long cabinetId) {
        return rendezVousRepository.findAllByCabinetId(cabinetId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    @Override
    public List<RendezVousResponse> getByMedecinAndDate(Long cabinetId, Long medecinId, LocalDate date) {
        return rendezVousRepository
                .findAllByCabinetIdAndMedecinIdAndDateRdvOrderByHeureRdvAsc(cabinetId, medecinId, date)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RendezVousResponse> getHistoriquePatient(Long cabinetId, Long patientId) {
        return rendezVousRepository
                .findAllByCabinetIdAndPatientIdOrderByDateRdvDescHeureRdvDesc(cabinetId, patientId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }




}
