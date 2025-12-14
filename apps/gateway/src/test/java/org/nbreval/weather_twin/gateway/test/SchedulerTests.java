package org.nbreval.weather_twin.gateway.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.in.SchedulerPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.port.out.OutputConnectorPort;
import org.nbreval.weather_twin.gateway.application.service.DispatcherService;
import org.nbreval.weather_twin.gateway.application.service.SchedulerService;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;

public class SchedulerTests {

  private static record TestFlush(
      String device,
      String sensor,
      long interval,
      Object value) {

  }

  private static ExpressionsDbPort expressionsDB;

  private static AggregationsDbPort aggregationsDB;

  private static WTALLogicPort wtalLogic;

  private static MeasureProcessorPort measureProcessor;

  private static DispatcherPort dispatcher;

  private static OutputConnectorPort outputConnector;

  private static SchedulerPort scheduler;

  private static List<TestFlush> outputs;

  @BeforeAll
  static void setup() throws IOException {
    var tmpFolder = Files.createTempDirectory("test").toAbsolutePath().toString();

    expressionsDB = new ExpressionsDbAdapter(Paths.get(tmpFolder, "aggregations").toAbsolutePath().toString(),
        Paths.get(tmpFolder, "flushes").toAbsolutePath().toString());

    aggregationsDB = new AggregationsDbAdapter();

    wtalLogic = new WtalLogicService();

    measureProcessor = new MeasureProcessorAdapter(wtalLogic);

    dispatcher = new DispatcherService(expressionsDB, aggregationsDB, measureProcessor);

    outputs = new ArrayList<>();

    outputConnector = new OutputConnectorPort() {

      @Override
      public void sendFlush(String device, String sensor, long interval, Object value) {
        outputs.add(new TestFlush(device, sensor, interval, value));
      }

    };

    scheduler = new SchedulerService(aggregationsDB, expressionsDB, measureProcessor, Set.of(outputConnector));
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
  void shouldAggregateDataSuccessfully() {
    // Register sensor with expression
    expressionsDB.setAggregatorExpression("device", "sensor", "curr + agg;");
    expressionsDB.setFlushExpression("device", "sensor", "agg / (steps - 1);");
    aggregationsDB.registerAggregation("device", "sensor", 1000, (int) 0);

    var values = List.of(1, 2, 3, 4, 5);
    values.forEach(n -> dispatcher.consume("device", "sensor", n).blockLast());

    scheduler.schedule(1000);

    Integer expected = values.stream().reduce((a, b) -> a + b).orElse(0) / values.size();

    Awaitility
        .await()
        .atMost(Duration.ofMinutes(1000))
        .until(() -> outputs.stream().anyMatch(o -> o.device().equals("device") && o.sensor().equals("sensor")
            && o.interval() == 1000L && o.value().equals(expected)));
  }

}
