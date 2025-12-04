package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.GatewayApplication;
import org.nbreval.weather_twin.gateway.test.config.DummySensorListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Test class to ensure the Spring Modulith's structure is being applied
 * correctly
 */
@SpringBootTest
@Import(DummySensorListener.class)
class GatewayApplicationTests {

  /**
   * Method used to configure Spring application without an application.yml file
   * 
   * @param registry Object to configure Spring's properties
   */
  @DynamicPropertySource
  static void setupProperties(DynamicPropertyRegistry registry) {

    // Important, this test class desn't test the logic, so it's needed to don't
    // start the MQTT server. The dummy listener type ensures the MQTT server is not
    // started.
    registry.add("entrypoint.listener.type", () -> "dummy");

  }

  /**
   * Uses the Spring's ApplicationModules class to ensure the project has the
   * correct Modulith's structure. The test package is excluded because it's not
   * necessary
   */
  @Test
  void checkModules() {
    var predicates = JavaClass.Predicates.resideInAPackage("org.nbreval.weather_twin.gateway.test.*");

    assertNotNull(predicates);

    ApplicationModules.of(GatewayApplication.class, predicates).verify();
  }

}
