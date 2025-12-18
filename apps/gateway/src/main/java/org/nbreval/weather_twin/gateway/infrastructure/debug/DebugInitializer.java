package org.nbreval.weather_twin.gateway.infrastructure.debug;

import java.util.Set;

import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;
import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DebugInitializer implements CommandLineRunner {

  private final SensorConfigurationPort sensorConfiguration;

  public DebugInitializer(SensorConfigurationPort sensorConfiguration) {
    this.sensorConfiguration = sensorConfiguration;
  }

  @Override
  public void run(String... args) throws Exception {
    if (sensorConfiguration.getAllRegistrations().isEmpty()) {
      // Add temperature sensor
      sensorConfiguration.registerSensor("my-device", "temp-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.TEMPERATURE, "ºC", "Sensor which measures the outside temperature");

      sensorConfiguration.registerSensor("my-device", "humidity-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.HUMIDITY, "%", "Sensor which measures the outside humidity");

      sensorConfiguration.registerSensor("my-device", "proximity-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.PROXIMITY, "%", "Sensor which measures the proximity to itself");

      sensorConfiguration.registerSensor("my-device", "acceleration-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.ACCELERATION, "%",
          "Sensor which measures the acceleration of an external object");

      sensorConfiguration.registerSensor("my-device", "giroscope-ext", "{\"x\": 0, \"y\": 0, \"z\": 0}",
          "{ \"x\": agg[\"x\"] + curr[\"x\"], \"y\": agg[\"y\"] + curr[\"y\"], \"z\": agg[\"z\"] + curr[\"z\"] };",
          "TERNARY(steps <= 1, default, {\"x\": agg[\"x\"] / (steps - 1), \"y\": agg[\"y\"] / (steps - 1), \"z\": agg[\"z\"] / (steps - 1)});",
          Set.of(5000L, 10000L), SensorType.GIROSCOPE, "xyz", "Sensor which measures angles of a giroscope");

      sensorConfiguration.registerSensor("my-device", "gas-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.GAS, "%/mL", "Sensor which measures the percent of a gas per mL");

      sensorConfiguration.registerSensor("my-device", "pressure-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.PRESSURE, "mbar", "Sensor which measures the pressure");

      sensorConfiguration.registerSensor("my-device", "level-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.LEVEL, "%", "Sensor which measures the capacity of a container");

      sensorConfiguration.registerSensor("my-device", "photoelectric-ext", "0.0", "agg + curr;",
          "TERNARY(steps <= 1, default, agg / (steps - 1));",
          Set.of(5000L, 10000L), SensorType.PHOTOELECTRIC, "%", "Sensor which measures the light intensity");

    }
  }

}
