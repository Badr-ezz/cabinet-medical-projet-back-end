package ma.cabinet.rendezvous_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cabinet.rendezvous_service.dto.FactureRequestDTO;
import ma.cabinet.rendezvous_service.dto.FactureResponseDTO;
import ma.cabinet.rendezvous_service.dto.FactureUpdateDTO;
import ma.cabinet.rendezvous_service.enums.StatutFacture;
import ma.cabinet.rendezvous_service.security.RoleAuthorizationInterceptor;
import ma.cabinet.rendezvous_service.service.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ma.cabinet.rendezvous_service.security.RoleAuthorizationInterceptor.RequireRole;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    @PostMapping
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> createFacture(@Valid @RequestBody FactureRequestDTO request) {
        FactureResponseDTO response = factureService.createFacture(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cabinet/{cabinetId}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<List<FactureResponseDTO>> getAllFacturesByCabinet(@PathVariable Long cabinetId) {
        return ResponseEntity.ok(factureService.getAllFacturesByCabinet(cabinetId));
    }

    @GetMapping("/cabinet/{cabinetId}/statut/{statut}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<List<FactureResponseDTO>> getFacturesByCabinetAndStatut(
            @PathVariable Long cabinetId,
            @PathVariable StatutFacture statut) {
        return ResponseEntity.ok(factureService.getFacturesByCabinetAndStatut(cabinetId, statut));
    }

    @GetMapping("/{factureId}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> getFactureById(@PathVariable Long factureId) {
        return ResponseEntity.ok(factureService.getFactureById(factureId));
    }

    @GetMapping("/rendezvous/{rendezVousId}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> getFactureByRendezVousId(@PathVariable Long rendezVousId) {
        return ResponseEntity.ok(factureService.getFactureByRendezVousId(rendezVousId));
    }

    @PutMapping("/{factureId}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> updateFacture(
            @PathVariable Long factureId,
            @Valid @RequestBody FactureUpdateDTO updateDTO) {
        return ResponseEntity.ok(factureService.updateFacture(factureId, updateDTO));
    }

    @PatchMapping("/{factureId}/payer")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> marquerCommePaye(@PathVariable Long factureId) {
        return ResponseEntity.ok(factureService.marquerCommePaye(factureId));
    }

    @PatchMapping("/{factureId}/annuler")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<FactureResponseDTO> annulerFacture(@PathVariable Long factureId) {
        return ResponseEntity.ok(factureService.annulerFacture(factureId));
    }

    @DeleteMapping("/{factureId}")
    @RequireRole({"SECRETARY"})
    public ResponseEntity<Void> deleteFacture(@PathVariable Long factureId) {
        factureService.deleteFacture(factureId);
        return ResponseEntity.noContent().build();
    }
}
