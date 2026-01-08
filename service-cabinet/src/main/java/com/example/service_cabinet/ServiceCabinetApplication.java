package com.example.service_cabinet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceCabinetApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCabinetApplication.class, args);
	}

}
