package com.cabinet.patientservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Patient Service API",
                version = "1.0.0",
                description = "Microservice for managing patients and their medical records (dossier médical).",
                contact = @Contact(
                        name = "Cabinet Medical",
                        email = "support@cabinet-medical.local"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local server")
        }
)
public class OpenAPIConfig {
}


