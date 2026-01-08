package com.cabinet.consultationservice.controller;

import com.cabinet.consultationservice.dto.OrdonnanceRequestDTO;
import com.cabinet.consultationservice.dto.OrdonnanceResponseDTO;
import com.cabinet.consultationservice.service.OrdonnanceService;
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
@RequestMapping("/api/ordonnances")
@Tag(name = "Ordonnances", description = "API for managing medical prescriptions (ordonnances)")
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;

    public OrdonnanceController(OrdonnanceService ordonnanceService) {
        this.ordonnanceService = ordonnanceService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new ordonnance",
            description = "Creates a new ordonnance (prescription) for a consultation. The ordonnance must be linked to an existing consultation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ordonnance created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdonnanceResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"type\": \"MEDICAMENT\", \"contenuLibre\": \"Prendre 2 comprimés par jour\", \"consultationId\": 1, \"createdAt\": \"2024-01-15T10:35:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Consultation not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<OrdonnanceResponseDTO> createOrdonnance(
            @Parameter(description = "Ordonnance request data", required = true)
            @Valid @RequestBody OrdonnanceRequestDTO requestDTO) {
        OrdonnanceResponseDTO response = ordonnanceService.createOrdonnance(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/consultation/{consultationId}")
    @Operation(
            summary = "Get all ordonnances for a consultation",
            description = "Retrieves all ordonnances (prescriptions) associated with a specific consultation, including their medicaments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of ordonnances",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdonnanceResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<List<OrdonnanceResponseDTO>> getOrdonnancesByConsultationId(
            @Parameter(description = "Consultation ID", required = true, example = "1")
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(ordonnanceService.getOrdonnancesByConsultationId(consultationId));
    }
}

