package com.cabinet.consultationservice.controller;

import com.cabinet.consultationservice.dto.ConsultationRequestDTO;
import com.cabinet.consultationservice.dto.ConsultationResponseDTO;
import com.cabinet.consultationservice.service.ConsultationService;
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
@RequestMapping("/api/consultations")
@Tag(name = "Consultations", description = "API for managing medical consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new consultation",
            description = "Creates a new consultation linked to a patient and rendez-vous. The consultation must have valid patientId and rendezVousId."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Consultation created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"patientId\": 1, \"rendezVousId\": 1, \"medecinId\": 1, \"type\": \"CONSULTATION\", \"dateConsultation\": \"2024-01-15\", \"examenClinique\": \"Examen clinique normal\", \"diagnostic\": \"Grippe\", \"createdAt\": \"2024-01-15T10:30:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation error",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<ConsultationResponseDTO> createConsultation(
            @Parameter(description = "Consultation request data", required = true)
            @Valid @RequestBody ConsultationRequestDTO requestDTO) {
        ConsultationResponseDTO response = consultationService.createConsultation(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get consultation by ID",
            description = "Retrieves a consultation with all its ordonnances and medicaments by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consultation found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Consultation not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<ConsultationResponseDTO> getConsultationById(
            @Parameter(description = "Consultation ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "Get all consultations for a patient",
            description = "Retrieves all consultations associated with a specific patient ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of consultations",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<List<ConsultationResponseDTO>> getConsultationsByPatientId(
            @Parameter(description = "Patient ID", required = true, example = "1")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getConsultationsByPatientId(patientId));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a consultation",
            description = "Deletes a consultation and all its associated ordonnances and medicaments (cascade delete)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Consultation deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Consultation not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<Void> deleteConsultation(
            @Parameter(description = "Consultation ID", required = true, example = "1")
            @PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}

