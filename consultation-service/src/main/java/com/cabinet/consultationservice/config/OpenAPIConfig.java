package com.cabinet.consultationservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Consultation Service API",
                version = "1.0.0",
                description = "Medical consultation, ordonnance and medicament management",
                contact = @Contact(
                        name = "Cabinet Medical",
                        email = "support@cabinet-medical.local"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Local server")
        }
)
public class OpenAPIConfig {
}

