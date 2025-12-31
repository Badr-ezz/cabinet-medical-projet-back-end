package ma.cabinet.rendezvous_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Rendez-Vous", description = "API for managing medical appointments (rendez-vous)")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    /**
     * CREATE - Créer un nouveau rendez-vous
     * Autorisé: SECRETARY, ADMIN
     */
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new medical appointment. Requires SECRETARY or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment created successfully",
                    content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @PostMapping
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> create(@Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.createRendezVous(request);
        return ResponseEntity.ok(response);
    }

    /**
     * READ ALL - Récupérer tous les rendez-vous
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get all appointments",
            description = "Retrieves all medical appointments. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
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
    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieves a specific appointment by its ID. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found",
                    content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/{id}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> getById(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long id) {
        RendezVousResponse response = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * UPDATE - Modifier un rendez-vous existant
     * Autorisé: SECRETARY, ADMIN
     */
    @Operation(
            summary = "Update an appointment",
            description = "Updates an existing appointment. Requires SECRETARY or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment updated successfully",
                    content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @PutMapping("/{id}")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> update(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long id,
            @Valid @RequestBody RendezVousRequest request) {
        RendezVousResponse response = rendezVousService.updateRendezVous(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE - Supprimer un rendez-vous
     * Autorisé: ADMIN uniquement
     */
    @Operation(
            summary = "Delete an appointment",
            description = "Deletes an appointment. Requires ADMIN role only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN"})
    public ResponseEntity<Void> delete(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET BY DATE - Récupérer tous les rendez-vous d'une date donnée
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get appointments by date",
            description = "Retrieves all appointments for a specific date. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/by-date")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByDate(
            @Parameter(description = "Date in ISO format (YYYY-MM-DD)", required = true, example = "2024-01-15")
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
    @Operation(
            summary = "Get appointments by doctor and date",
            description = "Retrieves all appointments for a specific doctor on a specific date. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/by-medecin-and-date")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByMedecinAndDate(
            @Parameter(description = "Doctor ID", required = true) @RequestParam("medecinId") Long medecinId,
            @Parameter(description = "Date in ISO format (YYYY-MM-DD)", required = true, example = "2024-01-15")
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
    @Operation(
            summary = "Confirm an appointment",
            description = "Confirms an appointment. Changes status to CONFIRME. Requires SECRETARY or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment confirmed successfully",
                    content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @PatchMapping("/{id}/confirmer")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> confirmer(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long id) {
        RendezVousResponse response = rendezVousService.confirmerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * ANNULER - Annuler un rendez-vous
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Cancel an appointment",
            description = "Cancels an appointment. Changes status to ANNULE. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully",
                    content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @PatchMapping("/{id}/annuler")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<RendezVousResponse> annuler(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long id) {
        RendezVousResponse response = rendezVousService.annulerRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET BY PATIENT - Tous les rendez-vous d'un patient
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get appointments by patient",
            description = "Retrieves all appointments for a specific patient. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/by-patient")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByPatient(
            @Parameter(description = "Patient ID", required = true) @RequestParam("patientId") Long patientId) {
        List<RendezVousResponse> list = rendezVousService.getRendezVousByPatient(patientId);
        return ResponseEntity.ok(list);
    }

    /**
     * GET ALL BY CABINET - Tous les rendez-vous d'un cabinet
     * Autorisé: SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get all appointments by cabinet",
            description = "Retrieves all appointments for a specific cabinet. Requires SECRETARY or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/cabinet/{cabinetId}")
    @RequireRole({"SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getAllByCabinet(
            @Parameter(description = "Cabinet ID", required = true) @PathVariable Long cabinetId) {
        List<RendezVousResponse> list = rendezVousService.getAllByCabinet(cabinetId);
        return ResponseEntity.ok(list);
    }

    /**
     * GET BY CABINET, MEDECIN AND DATE - Rendez-vous d'un médecin dans un cabinet à une date
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get appointments by cabinet, doctor and date",
            description = "Retrieves all appointments for a specific doctor in a specific cabinet on a specific date. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/cabinet/{cabinetId}/medecin/{medecinId}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getByCabinetMedecinDate(
            @Parameter(description = "Cabinet ID", required = true) @PathVariable Long cabinetId,
            @Parameter(description = "Doctor ID", required = true) @PathVariable Long medecinId,
            @Parameter(description = "Date in ISO format (YYYY-MM-DD)", required = true, example = "2024-01-15")
            @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<RendezVousResponse> list = rendezVousService.getByMedecinAndDate(cabinetId, medecinId, date);
        return ResponseEntity.ok(list);
    }

    /**
     * GET HISTORIQUE PATIENT - Historique des rendez-vous d'un patient dans un cabinet
     * Autorisé: MEDECIN, SECRETARY, ADMIN
     */
    @Operation(
            summary = "Get patient appointment history",
            description = "Retrieves the complete appointment history for a patient in a specific cabinet. Requires MEDECIN, SECRETARY, or ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    @GetMapping("/cabinet/{cabinetId}/patient/{patientId}")
    @RequireRole({"MEDECIN", "SECRETARY", "ADMIN"})
    public ResponseEntity<List<RendezVousResponse>> getHistoriquePatient(
            @Parameter(description = "Cabinet ID", required = true) @PathVariable Long cabinetId,
            @Parameter(description = "Patient ID", required = true) @PathVariable Long patientId) {
        List<RendezVousResponse> list = rendezVousService.getHistoriquePatient(cabinetId, patientId);
        return ResponseEntity.ok(list);
    }
}