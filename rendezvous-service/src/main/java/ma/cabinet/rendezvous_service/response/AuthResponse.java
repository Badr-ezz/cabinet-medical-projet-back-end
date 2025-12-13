package ma.cabinet.rendezvous_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;

    // Utiliser "tokenExpired" au lieu de "isTokenExpired"
    // pour éviter les problèmes de sérialisation Jackson
    @JsonProperty("tokenExpired")
    private boolean tokenExpired;

    private String error;
    private String userRole;
}