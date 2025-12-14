package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;

public class AggregatorExpressionDbTests {

  private static final Path tmpFolder;
  private static final Path aggregatorsDbPath;
  private static final Path flushesDbPath;
  private static final ExpressionsDbPort expressionsDB;

  static {
    try {
      tmpFolder = Files.createTempDirectory("tests");
      aggregatorsDbPath = Paths.get(tmpFolder.toString(), "aggregators");
      flushesDbPath = Paths.get(tmpFolder.toString(), "flushes");
      expressionsDB = new ExpressionsDbAdapter(
          aggregatorsDbPath.toString(),
          flushesDbPath.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  void shouldStoreAggregationExpressionCorrectly() throws IOException {
    var device = "my-device";
    var sensor = "my-sensor";
    var expected = "4 + 5;";
    expressionsDB.setAggregatorExpression(device, sensor, expected);

    assertTrue(Files.exists(aggregatorsDbPath));
    // Wal files shouldn't exist because the method calls commit immediately
    assertTrue(Files.list(tmpFolder)
        .noneMatch(f -> f.getFileName().toString().matches(aggregatorsDbPath.getFileName().toString() + ".wal.\\d+")));

    var stored = expressionsDB.getAggregatorExpression(device, sensor);

    assertEquals(expected, stored);

    expressionsDB.removeAggregatorExpression(device, sensor);

    stored = expressionsDB.getAggregatorExpression(device, sensor);

    assertNull(stored);
  }

  @Test
  void shouldStoreFlushExpressionCorrectly() throws IOException {
    var device = "my-device";
    var sensor = "my-sensor";
    var expected = "4 + 5;";
    expressionsDB.setFlushExpression(device, sensor, expected);

    assertTrue(Files.exists(aggregatorsDbPath));
    // Wal files shouldn't exist because the method calls commit immediately
    assertTrue(Files.list(tmpFolder)
        .noneMatch(f -> f.getFileName().toString().matches(flushesDbPath.getFileName().toString() + ".wal.\\d+")));

    var stored = expressionsDB.getFlushExpression(device, sensor);

    assertEquals(expected, stored);

    expressionsDB.removeFlushExpression(device, sensor);

    stored = expressionsDB.getFlushExpression(device, sensor);

    assertNull(stored);
  }

  @Test
  void shouldStoreMultipleAggregationExpressionsCorrectly() {

    for (int i = 0; i < 10; i++) {
      expressionsDB.setAggregatorExpression("device%d".formatted(i + 1), "sensor%d".formatted(i + 1),
          "agg + curr + %d".formatted(i + 1));
    }

    var allExpressions = expressionsDB.getAllAggregatorExpressions();

    assertFalse(allExpressions.isEmpty());
    assertEquals(10, allExpressions.size());

    for (int i = 0; i < 10; i++) {
      assertEquals("agg + curr + %d".formatted(i + 1),
          allExpressions.get("device%d".formatted(i + 1)).get("sensor%d".formatted(i + 1)));
    }
  }

  @Test
  void shouldStoreMultipleFlushExpressionsCorrectly() {

    for (int i = 0; i < 10; i++) {
      expressionsDB.setFlushExpression("device%d".formatted(i + 1), "sensor%d".formatted(i + 1),
          "agg + curr + %d".formatted(i + 1));
    }

    var allExpressions = expressionsDB.getAllFlushExpressions();

    assertFalse(allExpressions.isEmpty());
    assertEquals(10, allExpressions.size());

    for (int i = 0; i < 10; i++) {
      assertEquals("agg + curr + %d".formatted(i + 1),
          allExpressions.get("device%d".formatted(i + 1)).get("sensor%d".formatted(i + 1)));
    }
  }
}
