package org.nbreval.weather_twin.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class GatewayApplicationTests {

	@Test
	void checkModules() {
		ApplicationModules.of(GatewayApplication.class).verify();
	}

}
