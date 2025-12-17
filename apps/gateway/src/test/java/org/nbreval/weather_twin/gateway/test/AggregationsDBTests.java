package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.entity.Aggregation;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.entity.AggregationKey;
import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

import io.swagger.v3.core.util.Json;

public class AggregationsDBTests {

  static AggregationsDbPort aggregationsDB;

  @BeforeAll
  static void setup() throws IOException {
    var tmpFolder = Files.createTempDirectory("test").toAbsolutePath().toString();

    aggregationsDB = new AggregationsDbAdapter(Paths.get(tmpFolder, "aggregations").toAbsolutePath().toString());
  }

  @BeforeEach
  void cleanup() {
    aggregationsDB.getAllAgregations().entrySet().forEach(e -> {
      var interval = e.getKey();
      e.getValue().entrySet().forEach(e1 -> {
        var device = e1.getKey();
        e1.getValue().entrySet().forEach(e2 -> {
          var sensor = e2.getKey();
          aggregationsDB.unregisterAggregation(device, sensor, interval);
        });
      });
    });
    aggregationsDB.applyChanges();
  }

  @Test
  void shouldStoreAggregationCorrectly() {
    var device = "device";
    var sensor = "sensor";
    var interval = 1000L;

    var stored = aggregationsDB.getAllAgregations();

    assertTrue(stored.isEmpty());

    aggregationsDB.registerAggregation(device, sensor, interval, DataType.INTEGER, 0);

    var byInterval = aggregationsDB.getAggregations(device, sensor);

    assertEquals(1, byInterval.size());

    aggregationsDB.updateAggregation(device, sensor, interval, 10);

    byInterval = aggregationsDB.getAggregations(device, sensor);

    assertEquals(new Aggregation(DataType.INTEGER, 10, 0, 2), byInterval.get(interval));

    aggregationsDB.unregisterAggregation(device, sensor, interval);

    byInterval = aggregationsDB.getAggregations(device, sensor);

    assertTrue(byInterval.isEmpty());
  }

  @Test
  void shouldStoreMultipleAggregationsWithDifferentTypesCorrectly() {
    var testCases = Map.of(
        new AggregationKey("deviceA", "sensorA", 1000), 0,
        new AggregationKey("deviceA", "sensorA", 5000), 0,
        new AggregationKey("deviceA", "sensorB", 1000), "",
        new AggregationKey("deviceB", "sensorA", 10000), true,
        new AggregationKey("deviceB", "sensorB", 10000), 0f);

    testCases.forEach(
        (k, v) -> aggregationsDB.registerAggregation(k.device(), k.sensor(), k.interval(), getDataTypeFromValue(v), v));

    var stored = aggregationsDB.getAllAgregations();

    assertEquals(testCases.entrySet().stream().collect(Collectors.groupingBy(e -> e.getKey().interval())).size(),
        stored.size());

    testCases
        .forEach((k, v) -> assertEquals(aggregationsDB.getAggregations(k.device(), k.sensor()).get(k.interval()),
            new Aggregation(getDataTypeFromValue(v), v, v, 1)));
  }

  private DataType getDataTypeFromValue(Object value) {
    if (value instanceof Integer) {
      return DataType.INTEGER;
    } else if (value instanceof Float) {
      return DataType.FLOAT;
    } else if (value instanceof String) {
      return DataType.TEXT;
    } else if (value instanceof Boolean) {
      return DataType.BOOLEAN;
    } else if (value instanceof Json) {
      return DataType.JSON;
    } else {
      throw new IllegalArgumentException(
          "Invalid input value of type '%s'".formatted(value.getClass().getSimpleName()));
    }
  }
}
