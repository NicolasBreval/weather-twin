package org.nbreval.weather_twin.gateway;

import java.nio.ByteBuffer;
import java.time.Duration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@SpringBootTest
public class EntrypointMqttTests {
    
    private static final Sinks.Many<SensorNotification> sink = Sinks.many().unicast().onBackpressureBuffer();

    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("entrypoint.listener.type", () -> "mqtt");
    }

    @TestConfiguration
    static class TestEvents {

        @EventListener
        public void handleSensorNotification(SensorNotification event) {
            sink.tryEmitNext(event);
        }
    }

    @Test
    public void checkEvents() throws MqttException {
        try (var client = new MqttClient("tcp://localhost:1883", "test-client")) {
            client.connect();

            StepVerifier.create(sink.asFlux())
                .then(() -> {
                    try {
                        client.publish("device1/sensor1/1757225005830", new MqttMessage(doubleToBytes(23.5)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .expectNextMatches(e -> 
                    e.getDeviceId().equals("device1") &&
                    e.getSensorId().equals("sensor1") &&
                    e.getUtcTimestamp() == 1757225005830L &&
                    e.getMeasure() == 23.5
                )
                .thenCancel()
                .verify(Duration.ofSeconds(5));

            client.disconnect();
        }
    }

    private byte[] doubleToBytes(double num) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.putDouble(num);
        return buffer.array();
    }
}
