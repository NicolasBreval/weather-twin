package org.nbreval.weather_twin.gateway.entrypoint.adapter.in;

import java.util.List;
import java.util.Properties;

import org.nbreval.weather_twin.gateway.entrypoint.adapter.in.exception.SensorListenerAdapterException;
import org.nbreval.weather_twin.gateway.entrypoint.port.in.SensorListenerPort;
import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import io.moquette.BrokerConstants;
import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;

/**
 * An implementation of {@link SensorListenerPort} that listens for MQTT messages using an embedded MQTT broker.
 * This listener listen for messages on topics with the format: {deviceId}/{sensorId}/{timestamp}
 * and expects the payload to be a float representing the sensor measurement. Each message received is then
 * converted into a {@link SensorNotification} and published as a Spring event.
 * 
 * To enable this listener, it is necessary to set the property `entrypoint.listener.type` to `mqtt`.
 */
@Component
@ConditionalOnProperty(name = "entrypoint.listener.type", havingValue = "mqtt")
public class MqttSensorListenerAdapter extends SensorListenerPort<InterceptPublishMessage> {
    /** Logger object used to show log messages */
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttSensorListenerAdapter.class);
    
    /** MQTT broker instance used to listen for device messages */
    private final Server broker;

    public MqttSensorListenerAdapter(
        ApplicationEventPublisher eventPublisher,
        @Value("${entrypoint.listener.mqtt.port}") int port,
        @Value("${entrypoint.listener.mqtt.persistence-path:}") String persistencePath
    ) {
        super(eventPublisher);
        var configProps = new Properties();
        configProps.put("port", Integer.toString(port));
        configProps.put("host", "0.0.0.0");

        if (persistencePath != null && !persistencePath.isBlank()) {
            configProps.put(BrokerConstants.DEFAULT_PERSISTENT_PATH, persistencePath);
        }

        var config = new MemoryConfig(configProps);

        this.broker = new Server();
        try {
            this.broker.startServer(config, List.of(messageIntercepHandler()));
        } catch (Exception e) {
            throw new SensorListenerAdapterException("Failed to start MQTT broker", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.broker.stopServer();
    }

    @Override
    public SensorNotification convert(InterceptPublishMessage inputMessage) {
        var topic = inputMessage.getTopicName();

        if (!topic.matches("\\w+/\\w+/\\d+")) {
            throw new SensorListenerAdapterException("Invalid topic format: " + topic, null);
        }

        var topicParts = topic.split("/");
        var deviceId = topicParts[0];
        var sensorId = topicParts[1];
        var timestamp = Long.parseLong(topicParts[2]);
        var measure = inputMessage.getPayload().getDouble(0);
        
        return new SensorNotification(
            this,
            deviceId,
            sensorId,
            timestamp,
            measure
        );
    }

    /**
     * Creates a cycle of message interceptors to handle MQTT events
     * @return the message interceptor handler
     */
    private InterceptHandler messageIntercepHandler() {
        return new AbstractInterceptHandler() {

            @Override
            public String getID() {
                return "EmbeddedBrokerListener";
            }

            @Override
            public void onSessionLoopError(Throwable exception) {
                LOGGER.error("Session loop error in MQTT broker", exception);
            }

            @Override
            public void onPublish(InterceptPublishMessage msg) {
                publishEvent(msg);
            }
            
        };
    }

}
