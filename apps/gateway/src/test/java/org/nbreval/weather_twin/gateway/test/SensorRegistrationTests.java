package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.entity.SensorRegistration;
import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;

public class SensorRegistrationTests {

  @Test
  void shouldReturnCorrectParts() {
    var registration = new SensorRegistration("device", "sensor", "agg + curr;",
        "TERNARY(steps <= 1, agg, agg / (steps - 1))", "0", Set.of(1000L), SensorType.TEMPERATURE, "ºC", "");

    var aggParts = registration.getAggregationExpressionParts();
    var flushParts = registration.getFlushExpressionParts();

    assertArrayEquals(new String[] { "agg", " + ", "curr", ";" }, aggParts);
    assertArrayEquals(new String[] { "TERNARY", "(", "steps", " <= ", "1", ", ", "agg", ", ", "agg", " / (", "steps",
        " - ", "1", "))" }, flushParts);
  }

}
