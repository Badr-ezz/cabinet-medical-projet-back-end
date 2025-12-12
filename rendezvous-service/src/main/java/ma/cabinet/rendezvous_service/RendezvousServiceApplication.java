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

}
