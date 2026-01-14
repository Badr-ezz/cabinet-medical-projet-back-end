package ma.cabinet.rendezvous_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RendezvousServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RendezvousServiceApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner initData(
			ma.cabinet.rendezvous_service.repository.RendezVousRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				ma.cabinet.rendezvous_service.entity.RendezVous rdv = ma.cabinet.rendezvous_service.entity.RendezVous
						.builder()
						.dateRdv(java.time.LocalDate.now())
						.heureRdv(java.time.LocalTime.of(10, 30))
						.motif("Consultation de routine")
						.statut(ma.cabinet.rendezvous_service.enums.StatutRDV.CONFIRME)
						.notes("Premier rendez-vous de la journée")
						.medecinId(1L)
						.patientId(1L)
						.cabinetId(1L)
						.build();
				repository.save(rdv);
				System.out.println("Rendez-vous seeding completed: 1 confirmed RDV added.");
			}
		};
	}
}
