package ma.cabinet.rendezvous_service.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.cabinet.rendezvous_service.request.RendezVousRequest;
import ma.cabinet.rendezvous_service.response.RendezVousResponse;
import ma.cabinet.rendezvous_service.service.RendezVousService;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RendezVousResponse> create(@Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.createRendezVous(request);
        return ResponseEntity.ok(response);
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<RendezVousResponse>> getAll() {
        List<RendezVousResponse> list = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousResponse> getById(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.updateRendezVous(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/by-date")
    public ResponseEntity<List<RendezVousResponse>> getByDate(
            @RequestParam("date")
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate date) {

        List<RendezVousResponse> list = rendezVousService.getRendezVousByDate(date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-medecin-and-date")
    public ResponseEntity<List<RendezVousResponse>> getByMedecinAndDate(
            @RequestParam("medecinId") Long medecinId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<RendezVousResponse> list = rendezVousService.getRendezVousByMedecinAndDate(medecinId, date);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<RendezVousResponse> confirmer(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.confirmerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<RendezVousResponse> annuler(@PathVariable Long id) {
        RendezVousResponse response = rendezVousService.annulerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-patient")
    public ResponseEntity<List<RendezVousResponse>> getByPatient(
            @RequestParam("patientId") Long patientId) {

        List<RendezVousResponse> list = rendezVousService.getRendezVousByPatient(patientId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cabinet/{cabinetId}")
    public ResponseEntity<List<RendezVousResponse>> getAllByCabinet(
            @PathVariable Long cabinetId) {

        List<RendezVousResponse> list = rendezVousService.getAllByCabinet(cabinetId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cabinet/{cabinetId}/medecin/{medecinId}")
    public ResponseEntity<List<RendezVousResponse>> getByMedecinAndDate(
            @PathVariable Long cabinetId,
            @PathVariable Long medecinId,
            @RequestParam("date") String dateStr) {

        LocalDate date = LocalDate.parse(dateStr); // format ISO "2025-12-10"
        List<RendezVousResponse> list =
                rendezVousService.getByMedecinAndDate(cabinetId, medecinId, date);
        return ResponseEntity.ok(list);
    }

    // 3) Historique RDV d’un patient pour un cabinet
    @GetMapping("/cabinet/{cabinetId}/patient/{patientId}")
    public ResponseEntity<List<RendezVousResponse>> getHistoriquePatient(
            @PathVariable Long cabinetId,
            @PathVariable Long patientId) {

        List<RendezVousResponse> list =
                rendezVousService.getHistoriquePatient(cabinetId, patientId);
        return ResponseEntity.ok(list);
    }




}
