package com.example.service_cabinet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceCabinetApplication {

	public static void main(String[] args) {
		System.out.println("═══════════════════════════════════════════════");
		System.out.println("  Démarrage du Cabinet Service.. .");
		System.out.println("═══════════════════════════════════════════════");

		SpringApplication.run(ServiceCabinetApplication.class, args);

		System.out.println("═══════════════════════════════════════════════");
		System.out.println("  ✓ Cabinet Service démarré avec succès!");
		System.out.println("  Port: 8081");
		System.out.println("  URL: http://localhost:8081/api/cabinets");
		System.out.println("═══════════════════════════════════════════════");
	}

}
