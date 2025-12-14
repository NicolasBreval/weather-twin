package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.service.DispatcherService;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.CoapAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.entity.Aggregation;

public class CoapTests {

  private static ExpressionsDbPort expressionsDB;

  private static AggregationsDbPort aggregationsDB;

  private static WTALLogicPort wtalLogic;

  private static MeasureProcessorPort measureProcessor;

  private static DispatcherPort dispatcher;

  private static CoapAdapter coap;

  @Mock
  private OptionSet optionSet;

  @Mock
  private CoapExchange exchange;

  @BeforeAll
  static void setup() throws IOException {
    var tmpFolder = Files.createTempDirectory("test").toAbsolutePath().toString();

    expressionsDB = new ExpressionsDbAdapter(Paths.get(tmpFolder, "aggregators").toAbsolutePath().toString(),
        Paths.get(tmpFolder, "flushes").toAbsolutePath().toString());

    aggregationsDB = new AggregationsDbAdapter(Paths.get(tmpFolder, "aggregations").toAbsolutePath().toString());

    wtalLogic = new WtalLogicService();

    measureProcessor = new MeasureProcessorAdapter(wtalLogic);

    dispatcher = new DispatcherService(expressionsDB, aggregationsDB, measureProcessor);

    coap = new CoapAdapter(dispatcher);
  }

  @BeforeEach
  void cleanup() {
    MockitoAnnotations.openMocks(this);

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
  void shouldCompleteFlowSuccessfully() {
    // Register sensor with expression
    expressionsDB.setAggregatorExpression("device", "sensor", "curr + agg;");
    aggregationsDB.registerAggregation("device", "sensor", 1000, (int) 0);
    aggregationsDB.registerAggregation("device", "sensor", 5000, (int) 0);
    aggregationsDB.registerAggregation("device", "sensor", 10000, (int) 0);

    // Mock test request
    when(optionSet.getUriPathString()).thenReturn("measures/device/sensor");
    when(exchange.getQueryParameter("data_type")).thenReturn("INTEGER");
    when(exchange.getRequestOptions()).thenReturn(optionSet);
    when(exchange.getRequestPayload()).thenReturn("23".getBytes());

    coap.handlePOST(exchange);

    verify(exchange, times(1)).respond(eq(ResponseCode.CREATED));

    // Check aggregation was successfull
    assertEquals(new Aggregation(23, 0, 2), aggregationsDB.getAggregation("device", "sensor", 1000));
    assertEquals(new Aggregation(23, 0, 2), aggregationsDB.getAggregation("device", "sensor", 5000));
    assertEquals(new Aggregation(23, 0, 2), aggregationsDB.getAggregation("device", "sensor", 10000));
  }

  @Test
  void shouldFailDueToBadUri() {
    // Mock test request
    when(optionSet.getUriPathString()).thenReturn("bad_uri");
    when(exchange.getQueryParameter("data_type")).thenReturn("INTEGER");
    when(exchange.getRequestOptions()).thenReturn(optionSet);
    when(exchange.getRequestPayload()).thenReturn("23".getBytes());

    coap.handlePOST(exchange);

    verify(exchange, times(1)).respond(eq(ResponseCode.BAD_REQUEST), anyString());
  }

  @Test
  void shouldFailDueToBadQueryParams() {
    // Mock test request
    when(optionSet.getUriPathString()).thenReturn("measures/device/sensor");
    when(exchange.getRequestOptions()).thenReturn(optionSet);
    when(exchange.getRequestPayload()).thenReturn("23".getBytes());

    coap.handlePOST(exchange);

    when(exchange.getQueryParameter("data_type")).thenReturn("INTEGE");

    coap.handlePOST(exchange);

    verify(exchange, times(2)).respond(eq(ResponseCode.BAD_REQUEST), anyString());
  }

  @Test
  void shouldFailDueToBadPayload() {
    // Mock test request
    when(optionSet.getUriPathString()).thenReturn("measures/device/sensor");
    when(exchange.getQueryParameter("data_type")).thenReturn("INTEGER");
    when(exchange.getRequestOptions()).thenReturn(optionSet);

    coap.handlePOST(exchange);

    verify(exchange, times(1)).respond(eq(ResponseCode.BAD_REQUEST), anyString());
  }

  @Test
  void shouldFailDueToProcessingError() {
    // Register sensor with expression
    expressionsDB.setAggregatorExpression("device", "sensor", "curr + agg");
    aggregationsDB.registerAggregation("device", "sensor", 1000, (int) 0);
    aggregationsDB.registerAggregation("device", "sensor", 5000, (int) 0);
    aggregationsDB.registerAggregation("device", "sensor", 10000, (int) 0);

    // Mock test request
    when(optionSet.getUriPathString()).thenReturn("measures/device/sensor");
    when(exchange.getQueryParameter("data_type")).thenReturn("INTEGER");
    when(exchange.getRequestOptions()).thenReturn(optionSet);
    when(exchange.getRequestPayload()).thenReturn("23".getBytes());

    coap.handlePOST(exchange);

    expressionsDB.setAggregatorExpression("device", "sensor", "curr + ag;");

    coap.handlePOST(exchange);

    verify(exchange, times(2)).respond(eq(ResponseCode.INTERNAL_SERVER_ERROR));
  }

}
