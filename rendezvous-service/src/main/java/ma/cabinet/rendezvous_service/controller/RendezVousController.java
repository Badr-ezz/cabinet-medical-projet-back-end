package ma.cabinet.rendezvous_service.controller;

import jakarta.validation.Valid;
import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import ma.cabinet.rendezvous_service.response.RendezVousResponse;
import ma.cabinet.rendezvous_service.security.RoleAuthorizationInterceptor.RequireRole;
import ma.cabinet.rendezvous_service.service.RendezVousService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    /**
     * CREATE - Créer un nouveau rendez-vous
     * Autorisé: SECRETARY, ADMIN
     */
    @PostMapping
    @RequireRole({"SECRETARY"})
    public ResponseEntity<RendezVousResponse> create(@Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.createRendezVous(request);
        return ResponseEntity.ok(response);
    }

    /**
     * READ ALL - Récupérer tous les rendez-vous
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/all")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getAll() {
        List<RendezVousResponse> list = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(list);
    }

    /**
     * READ BY ID - Récupérer un rendez-vous par son ID
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/{id}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> getById(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * UPDATE - Modifier un rendez-vous existant
     * Autorisé: SECRETARY, ADMIN
     */
    @PutMapping("/{id}")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.updateRendezVous(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE - Supprimer un rendez-vous
     * Autorisé: ADMIN uniquement
     */
    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET BY DATE - Récupérer tous les rendez-vous d'une date donnée
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/by-date")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        List<RendezVousResponse> list = rendezVousService.getRendezVousByDate(date);
        return ResponseEntity.ok(list);
    }

    /**
     * GET BY MEDECIN AND DATE - Rendez-vous d'un médecin à une date donnée
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/by-medecin-and-date")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByMedecinAndDate(
            @RequestParam("medecinId") Long medecinId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        List<RendezVousResponse> list = rendezVousService.getRendezVousByMedecinAndDate(medecinId, date);
        return ResponseEntity.ok(list);
    }

    /**
     * CONFIRMER - Confirmer un rendez-vous
     * Autorisé: SECRETARY, ADMIN
     */
    @PatchMapping("/{id}/confirmer")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> confirmer(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.confirmerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * ANNULER - Annuler un rendez-vous
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @PatchMapping("/{id}/annuler")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> annuler(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.annulerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET BY PATIENT - Tous les rendez-vous d'un patient
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/by-patient")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByPatient(
            @RequestParam("patientId") Long patientId) {
        List<RendezVousResponse> list = rendezVousService.getRendezVousByPatient(patientId);
        return ResponseEntity.ok(list);
    }

    /**
     * GET ALL BY CABINET - Tous les rendez-vous d'un cabinet
     * Autorisé: SECRETARY, ADMIN
     */
    @GetMapping("/cabinet/{cabinetId}")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getAllByCabinet(
            @PathVariable Long cabinetId) {
        List<RendezVousResponse> list = rendezVousService.getAllByCabinet(cabinetId);
        return ResponseEntity.ok(list);
    }

    /**
     * GET BY CABINET, MEDECIN AND DATE - Rendez-vous d'un médecin dans un cabinet à une date
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/cabinet/{cabinetId}/medecin/{medecinId}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByCabinetMedecinDate(
            @PathVariable Long cabinetId,
            @PathVariable Long medecinId,
            @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<RendezVousResponse> list = rendezVousService.getByMedecinAndDate(cabinetId, medecinId, date);
        return ResponseEntity.ok(list);
    }

    /**
     * GET HISTORIQUE PATIENT - Historique des rendez-vous d'un patient dans un cabinet
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @GetMapping("/cabinet/{cabinetId}/patient/{patientId}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getHistoriquePatient(
            @PathVariable Long cabinetId,
            @PathVariable Long patientId) {
        List<RendezVousResponse> list = rendezVousService.getHistoriquePatient(cabinetId, patientId);
        return ResponseEntity.ok(list);
    }
}