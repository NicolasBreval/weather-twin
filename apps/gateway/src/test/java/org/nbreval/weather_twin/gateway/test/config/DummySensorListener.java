package org.nbreval.weather_twin.gateway.test.config;

import org.nbreval.weather_twin.gateway.entrypoint.port.in.SensorListenerPort;
import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

/**
 * A test configuration that provides a dummy implementation of {@link SensorListenerPort}
 * for testing purposes. This dummy listener does not perform any real listening but can be
 * used to satisfy dependencies in tests. It is activated when the property
 * `entrypoint.listener.type` is set to `dummy`.
 */
@TestConfiguration
public class DummySensorListener {
    
    /**
     * Creates a dummy implementation of {@link SensorListenerPort} for testing.
     * This implementation does not perform any real listening but can be used
     * to satisfy dependencies in tests.
     * @param publisher the application event publisher
     * @return a dummy sensor listener port
     */
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
