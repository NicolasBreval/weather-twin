package org.nbreval.weather_twin.gateway.test;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.entity.Aggregation;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

import reactor.test.StepVerifier;

public class MeasureProcessorTests {
  static WTALLogicPort wtalLogic;

  static MeasureProcessorPort measureProcessor;

  @BeforeAll
  static void setup() throws IOException {
    wtalLogic = new WtalLogicService();
    measureProcessor = new MeasureProcessorAdapter(wtalLogic);
  }

  @Test
  void shouldProcessMeasureCorrectly() {
    var device = "device";
    var sensor = "sensor";

    var aggregations = new HashMap<Long, Aggregation>();
    aggregations.put(1000L, new Aggregation(DataType.INTEGER, 0, 0, 1));
    aggregations.put(5000L, new Aggregation(DataType.INTEGER, 0, 0, 1));
    aggregations.put(10000L, new Aggregation(DataType.INTEGER, 0, 0, 1));

    var verifier = StepVerifier
        .create(measureProcessor.aggregateMeasure(device, sensor, 1, aggregations, "agg + curr;"));

    aggregations.forEach((interval, _) -> verifier.expectNextMatches(received -> {
      return aggregations.containsKey(received.getKey()) && received.getValue().equals(1);
    }));

    verifier
        .expectComplete()
        .verify();
  }

}
