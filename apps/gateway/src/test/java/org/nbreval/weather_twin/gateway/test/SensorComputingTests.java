package org.nbreval.weather_twin.gateway.test;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.shared.domain.model.MeanMeasureEvent;
import org.nbreval.weather_twin.gateway.test.util.HttpUtils;
import org.nbreval.weather_twin.gateway.test.util.TypeUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.FileSystemUtils;

import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

/**
 * Test class to ensure the computing module works correctly
 */
@TestPropertySource(properties = "logging.level.org.nbreval.weather_twin=DEBUG")
@SpringBootTest
public class SensorComputingTests {

  /**
   * Sink object used to redirect all MeanMeasure events and process them later
   */
  private static final Sinks.Many<MeanMeasureEvent> sink = Sinks.many().unicast().onBackpressureBuffer();

  /**
   * Folder where store persisted elements from MQTT server
   */
  private static final String MQTT_PERSISTENCE_PATH = "data/%s".formatted(UUID.randomUUID().toString());

  /**
   * Port used to configure MQTT server
   */
  private static final int MQTT_PORT = HttpUtils.getRandomPort();

  /**
   * Method used to configure Spring application without an application.yml file.
   * 
   * @param registry Object used to configure each property
   */
  @DynamicPropertySource
  static void setupProperties(DynamicPropertyRegistry registry) {
    registry.add("entrypoint.listener.type", () -> "mqtt");
    registry.add("entrypoint.listener.mqtt.port", () -> MQTT_PORT);
    registry.add("entrypoint.listener.mqtt.persistence-path", () -> MQTT_PERSISTENCE_PATH);
    registry.add("computing.batching.window-list", () -> "30s");
  }

  /**
   * Method used to clean the MQTT persistence folder, to ensure the next test
   * which needs the MQTT server starts with a clean environment
   */
  @AfterAll
  static void teardown() {
    FileSystemUtils.deleteRecursively(Paths.get(MQTT_PERSISTENCE_PATH).toFile());
  }

  /**
   * Util class used to store all events received from computing module into the
   * {@link sink} and process them later
   */
  @TestConfiguration
  static class TestEvents {

    /**
     * Listens for all MeanMeasure events and stores them into the {@link sink}
     * 
     * @param event
     */
    @EventListener
    public void handleMeanMasureEvents(MeanMeasureEvent event) {
      sink.tryEmitNext(event);
    }
  }

  /**
   * Test to ensure the computing module calculates correctly the mean of a device
   * which sends data periodically
   * 
   * @throws MqttException Thrown if occurs any error during MQTT connection
   */
  @Test
  public void checkMeasure() throws MqttException {
    try (var client = new MqttClient("tcp://localhost:%d".formatted(MQTT_PORT), "test-client")) {
      client.connect();

      var random = new Random();
      var tsList = new CopyOnWriteArrayList<Long>();
      var measureList = new CopyOnWriteArrayList<Double>();

      StepVerifier.create(sink.asFlux())
          .then(() -> IntStream.range(0, 5).forEach(i -> {
            try {
              var measure = random.nextDouble() * i;
              var ts = System.currentTimeMillis();
              client.publish("device1/sensor1/%d".formatted(ts),
                  new MqttMessage(TypeUtils.doubleToBytes(measure)));
              measureList.add(measure);
              tsList.add(ts);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }))
          .expectNextMatches(e -> {
            var currentDevice = e.getDeviceId();
            var expectedDevice = "device1";

            var currentSensor = e.getSensorId();
            var expectedSensor = "sensor1";

            var currentTs = e.getUtcTimestamp();
            var expectedTs = tsList.getFirst();

            var currentMeasure = e.getMeasure();
            var expectedMeasure = measureList.stream().reduce(0d,
                (a, b) -> Optional.ofNullable(a).orElse(0d) + Optional.ofNullable(b).orElse(0d))
                / measureList.size();

            var currentWs = e.getWindowSize();
            var expectedWs = 30000L;

            return currentDevice.equals(expectedDevice) && currentSensor.equals(expectedSensor)
                && currentTs == expectedTs && currentMeasure == expectedMeasure
                && currentWs == expectedWs;
          })
          .thenCancel()
          .verify(Duration.ofMinutes(1));

      client.disconnect();
    }
  }
}
