package org.nbreval.weather_twin.gateway.test.config;

import org.nbreval.weather_twin.gateway.entrypoint.port.in.SensorListenerPort;
import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DummySensorListener {
    
    @Bean
    @ConditionalOnProperty(name = "entrypoint.listener.type", havingValue = "dummy")
    public SensorListenerPort<String> sensorListenerPort(ApplicationEventPublisher publisher) {
        return new SensorListenerPort<String>(publisher) {
            @Override
            public void destroy() throws Exception {
                // Nothing to destroy
            }

            @Override
            protected SensorNotification convert(String inputMessage) {
                return new SensorNotification(this, "dummy", "dummy", 0, 0);
            }
        };
    }

}
