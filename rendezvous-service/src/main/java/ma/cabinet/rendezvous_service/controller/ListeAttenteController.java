package ma.cabinet.rendezvous_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.cabinet.rendezvous_service.response.ListeAttenteResponse;
import ma.cabinet.rendezvous_service.service.ListeAttenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/liste-attente")
@Tag(name = "Liste d'Attente", description = "API for managing waiting lists (liste d'attente)")
public class ListeAttenteController {

    private final ListeAttenteService listeAttenteService;

    public ListeAttenteController(ListeAttenteService listeAttenteService) {
        this.listeAttenteService = listeAttenteService;
    }

    /**
     * Add patient to waiting list when they arrive at the cabinet
     */
    @Operation(
            summary = "Add patient to waiting list",
            description = "Adds a patient to the waiting list when they arrive at the cabinet. Uses the appointment ID to create a waiting list entry."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient added to waiting list successfully",
                    content = @Content(schema = @Schema(implementation = ListeAttenteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/{rendezVousId}")
    public ResponseEntity<ListeAttenteResponse> ajouterAListeAttente(
            @Parameter(description = "Appointment ID", required = true) @PathVariable Long rendezVousId) {

        ListeAttenteResponse response = listeAttenteService.ajouterAListeAttente(rendezVousId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get today's waiting list for a doctor",
            description = "Retrieves the waiting list for a specific doctor for today's date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Waiting list retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/medecin/{medecinId}/today")
    public ResponseEntity<List<ListeAttenteResponse>> getListeAttenteMedecinAujourdHui(
            @Parameter(description = "Doctor ID", required = true) @PathVariable Long medecinId,
            @Parameter(description = "Cabinet ID", required = true) @RequestParam("cabinetId") Long cabinetId) {

        LocalDate today = LocalDate.now();
        List<ListeAttenteResponse> list =
                listeAttenteService.getListeAttentePourMedecinEtDate(cabinetId, medecinId, today);

        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Send patient to doctor",
            description = "Marks a patient in the waiting list as being sent to the doctor. Updates the status to indicate the patient is with the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient sent to doctor successfully",
                    content = @Content(schema = @Schema(implementation = ListeAttenteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Waiting list entry not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PatchMapping("/{id}/envoyer-au-medecin")
    public ResponseEntity<ListeAttenteResponse> envoyerAuMedecin(
            @Parameter(description = "Waiting list entry ID", required = true) @PathVariable Long id) {

        ListeAttenteResponse response = listeAttenteService.envoyerAuMedecin(id);
        return ResponseEntity.ok(response);
    }
}
