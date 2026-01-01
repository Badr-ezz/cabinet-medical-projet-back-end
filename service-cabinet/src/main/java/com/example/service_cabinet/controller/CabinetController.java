package com.example.service_cabinet.controller;




import com.example.service_cabinet.dto.CabinetDTO;
import com.example.service_cabinet.dto.CreateCabinetRequest;
import com.example.service_cabinet.service.CabinetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework. http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabinets")
public class CabinetController {

    private final CabinetService cabinetService;

    public CabinetController(CabinetService cabinetService) {
        this.cabinetService = cabinetService;
    }

    // POST:  Créer un nouveau cabinet
    @PostMapping
    public ResponseEntity<CabinetDTO> createCabinet(@Valid @RequestBody CreateCabinetRequest request) {
        System.out.println("DEBUG: Requête POST /api/cabinets - Création cabinet: " + request.getNom());
        CabinetDTO createdCabinet = cabinetService.createCabinet(request);
        return new ResponseEntity<>(createdCabinet, HttpStatus.CREATED);
    }

    // GET:  Récupérer tous les cabinets
    @GetMapping
    public ResponseEntity<List<CabinetDTO>> getAllCabinets() {
        System.out.println("DEBUG: Requête GET /api/cabinets - Récupération tous cabinets");
        List<CabinetDTO> cabinets = cabinetService.getAllCabinets();
        return ResponseEntity.ok(cabinets);
    }

    // GET:  Récupérer les cabinets actifs
    @GetMapping("/active")
    public ResponseEntity<List<CabinetDTO>> getActiveCabinets() {
        System.out.println("DEBUG: Requête GET /api/cabinets/active - Récupération cabinets actifs");
        List<CabinetDTO> activeCabinets = cabinetService.getActiveCabinets();
        return ResponseEntity.ok(activeCabinets);
    }

    // GET:  Récupérer un cabinet par ID
    @GetMapping("/{id}")
    public ResponseEntity<CabinetDTO> getCabinetById(@PathVariable Long id) {
        System.out.println("DEBUG: Requête GET /api/cabinets/" + id);
        CabinetDTO cabinet = cabinetService.getCabinetById(id);
        return ResponseEntity.ok(cabinet);
    }

    // PUT: Mettre à jour un cabinet
    @PutMapping("/{id}")
    public ResponseEntity<CabinetDTO> updateCabinet(
            @PathVariable Long id,
            @Valid @RequestBody CreateCabinetRequest request) {
        System.out.println("DEBUG: Requête PUT /api/cabinets/" + id);
        CabinetDTO updatedCabinet = cabinetService.updateCabinet(id, request);
        return ResponseEntity.ok(updatedCabinet);
    }

    // DELETE: Désactiver un cabinet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCabinet(@PathVariable Long id) {
        System.out.println("DEBUG: Requête DELETE /api/cabinets/" + id);
        cabinetService. deleteCabinet(id);
        return ResponseEntity.noContent().build();
    }

    // Exception handler global
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        System.err.println("ERREUR:  " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("ERREUR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Classe pour les réponses d'erreur
    public static class ErrorResponse {
        private String status;
        private String message;

        public ErrorResponse(String status, String message) {
            this. status = status;
            this. message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
