package com.cabinet.consultationservice.controller;

import com.cabinet.consultationservice.dto.MedicamentRequestDTO;
import com.cabinet.consultationservice.dto.MedicamentResponseDTO;
import com.cabinet.consultationservice.service.MedicamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicaments")
@Tag(name = "Medicaments", description = "API for managing medications")
public class MedicamentController {

    private final MedicamentService medicamentService;

    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @PostMapping
    @Operation(
            summary = "Add a medicament to an ordonnance",
            description = "Creates a new medicament and associates it with an ordonnance. The medicament must be linked to an existing ordonnance."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Medicament created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicamentResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nom\": \"Paracétamol\", \"description\": \"Antalgique et antipyrétique\", \"dosage\": \"500mg\", \"duree\": \"7 jours\", \"actif\": true, \"ordonnanceId\": 1}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ordonnance not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<MedicamentResponseDTO> createMedicament(
            @Parameter(description = "Medicament request data", required = true)
            @Valid @RequestBody MedicamentRequestDTO requestDTO) {
        MedicamentResponseDTO response = medicamentService.createMedicament(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search medicaments by name",
            description = "Searches for active medicaments by name (case-insensitive, partial match). Returns only active medicaments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of matching medicaments",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicamentResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<List<MedicamentResponseDTO>> searchMedicamentsByName(
            @Parameter(description = "Medicament name (partial match)", required = true, example = "para")
            @RequestParam("nom") String nom) {
        return ResponseEntity.ok(medicamentService.searchMedicamentsByName(nom));
    }

    @PutMapping("/{id}/disable")
    @Operation(
            summary = "Disable a medicament (soft delete)",
            description = "Disables a medicament by setting its 'actif' field to false. This is a soft delete operation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Medicament disabled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Medicament not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<Void> disableMedicament(
            @Parameter(description = "Medicament ID", required = true, example = "1")
            @PathVariable Long id) {
        medicamentService.disableMedicament(id);
        return ResponseEntity.ok().build();
    }
}

