package org.nbreval.weather_twin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Gateway application.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class GatewayApplication {

	/**
	 * Main method to run the Gateway application.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
