package org.nbreval.weather_twin.gateway.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.service.DispatcherService;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;

import reactor.test.StepVerifier;

public class DispatcherTests {

  static ExpressionsDbPort expressionsDB;

  static AggregationsDbPort aggregationsDB;

  static WTALLogicPort wtalLogic;

  static MeasureProcessorPort measureProcessor;

  static DispatcherPort dispatcher;

  @BeforeAll
  static void setup() throws IOException {
    var tmpFolder = Files.createTempDirectory("tests");
    var aggregatorsLocation = Paths.get(tmpFolder.toAbsolutePath().toString(), "aggregators").toString();
    var flushesLocation = Paths.get(tmpFolder.toAbsolutePath().toString(), "flushes").toString();

    expressionsDB = new ExpressionsDbAdapter(aggregatorsLocation, flushesLocation);
    aggregationsDB = new AggregationsDbAdapter(
        Paths.get(tmpFolder.toAbsolutePath().toString(), "aggregations").toString());
    wtalLogic = new WtalLogicService();
    measureProcessor = new MeasureProcessorAdapter(wtalLogic);
    dispatcher = new DispatcherService(expressionsDB, aggregationsDB, measureProcessor);
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

    expressionsDB.getAllAggregatorExpressions().entrySet().forEach(e -> {
      var device = e.getKey();
      e.getValue().entrySet().forEach(e1 -> {
        var sensor = e1.getKey();
        expressionsDB.removeAggregatorExpression(device, sensor);
      });
    });
  }

  @Test
  void shouldProcessIntMessageSuccessfully() {
    var device = "device";
    var sensor = "sensor";

    // Register expression
    expressionsDB.setAggregatorExpression(device, sensor, "agg + curr;");

    // Register aggregations
    aggregationsDB.registerAggregation(device, sensor, 1000, 0);
    aggregationsDB.registerAggregation(device, sensor, 5000, 0);
    aggregationsDB.registerAggregation(device, sensor, 10000, 0);

    // Send new value to aggregate
    var stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", 1));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(1);
      });
    }

    stepVerifier.verifyComplete();

    // Send another value
    stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", 4));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(5);
      });
    }

    stepVerifier.verifyComplete();
  }

  @Test
  void shouldProcessFloatMessageSuccessfully() {
    var device = "device";
    var sensor = "sensor";

    // Register expression
    expressionsDB.setAggregatorExpression(device, sensor, "agg + curr;");

    // Register aggregations
    aggregationsDB.registerAggregation(device, sensor, 1000, 0);
    aggregationsDB.registerAggregation(device, sensor, 5000, 0);
    aggregationsDB.registerAggregation(device, sensor, 10000, 0);

    // Send new value to aggregate
    var stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", 1.5f));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(1.5f);
      });
    }

    stepVerifier.verifyComplete();

    // Send another value
    stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", 4.3f));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(5.8f);
      });
    }

    stepVerifier.verifyComplete();
  }

  @Test
  void shouldProcessStringMessageSuccessfully() {
    var device = "device";
    var sensor = "sensor";

    // Register expression
    expressionsDB.setAggregatorExpression(device, sensor, "agg + curr;");

    // Register aggregations
    aggregationsDB.registerAggregation(device, sensor, 1000, "");
    aggregationsDB.registerAggregation(device, sensor, 5000, "");
    aggregationsDB.registerAggregation(device, sensor, 10000, "");

    // Send new value to aggregate
    var stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", "ho"));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals("ho");
      });
    }

    stepVerifier.verifyComplete();

    // Send another value
    stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", "la"));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals("hola");
      });
    }

    stepVerifier.verifyComplete();
  }

  @Test
  void shouldProcessBooleanMessageSuccessfully() {
    var device = "device";
    var sensor = "sensor";

    // Register expression
    expressionsDB.setAggregatorExpression(device, sensor, "agg & curr;");

    // Register aggregations
    aggregationsDB.registerAggregation(device, sensor, 1000, true);
    aggregationsDB.registerAggregation(device, sensor, 5000, true);
    aggregationsDB.registerAggregation(device, sensor, 10000, true);

    // Send new value to aggregate
    var stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", true));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(true);
      });
    }

    stepVerifier.verifyComplete();

    // Send another value
    stepVerifier = StepVerifier.create(dispatcher.consume("device", "sensor", false));

    for (int i = 0; i < 3; i++) {
      stepVerifier.expectNextMatches(entry -> {
        var interval = entry.getKey();
        var value = entry.getValue();

        return (interval.equals(1000L) || interval.equals(5000L) || interval.equals(10000L)) && value.equals(false);
      });
    }

    stepVerifier.verifyComplete();
  }

}
