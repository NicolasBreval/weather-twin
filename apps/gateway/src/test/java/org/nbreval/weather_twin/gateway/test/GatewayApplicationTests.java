package org.nbreval.weather_twin.gateway.test;

import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.GatewayApplication;
import org.nbreval.weather_twin.gateway.test.config.DummySensorListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.tngtech.archunit.core.domain.JavaClass;

@SpringBootTest
@Import(DummySensorListener.class)
class GatewayApplicationTests {

	@DynamicPropertySource
	static void setupProperties(DynamicPropertyRegistry registry) {
		registry.add("entrypoint.listener.type", () -> "dummy");

	}

	@Test
	void checkModules() {
		ApplicationModules.of(GatewayApplication.class, JavaClass.Predicates.resideInAPackage("org.nbreval.weather_twin.gateway.test.*")).verify();
	}

}
