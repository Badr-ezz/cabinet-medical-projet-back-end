package ma.cabinet.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // ✅ Origine exacte (pas de *)
        corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));

        // ✅ Méthodes HTTP
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // ✅ Tous les headers
        corsConfig.setAllowedHeaders(List.of("*"));

        // ✅ Credentials (important pour les cookies/tokens)
        corsConfig.setAllowCredentials(true);

        // ✅ Exposer Authorization au frontend
        corsConfig.setExposedHeaders(List.of("Authorization"));

        // ✅ Cache preflight 1h
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}