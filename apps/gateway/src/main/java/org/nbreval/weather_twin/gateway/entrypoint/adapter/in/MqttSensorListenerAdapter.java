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

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;

@Component
@ConditionalOnProperty(name = "entrypoint.listener.type", havingValue = "mqtt")
public class MqttSensorListenerAdapter extends SensorListenerPort<InterceptPublishMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttSensorListenerAdapter.class);
    
    private final Server broker;

    public MqttSensorListenerAdapter(
        ApplicationEventPublisher eventPublisher,
        @Value("${entrypoint.listener.mqtt.port}") int port
    ) {
        super(eventPublisher);
        var configProps = new Properties();
        configProps.put("port", Integer.toString(port));
        configProps.put("host", "0.0.0.0");

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
