package org.nbreval.weather_twin.gateway.entrypoint.port.in;

import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;

/**
 * An abstract port defining the contract for listening to sensor messages from devices.
 * It uses Spring's event publishing mechanism to publish {@link SensorNotification} events
 * when a new message is received. Concrete implementations must provide the logic to convert
 * the input message type {@code T} into a {@link SensorNotification}.
 *
 * @param <T> The type of the input message that the listener will handle.
 */
public abstract class SensorListenerPort<T> implements DisposableBean {

    /** Object used to publish Spring's events */
    protected final ApplicationEventPublisher eventPublisher;

    public SensorListenerPort(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Transforms the input message of type {@code T} into a {@link SensorNotification}.
     * Concrete implementations must provide the conversion logic.
     * @param inputMessage the input message to be converted
     * @return the corresponding {@link SensorNotification}
     */
    protected abstract SensorNotification convert(T inputMessage);

    /**
     * Converts and sends the input message as a Spring event.
     * @param inputMessage the input message to be published
     */
    protected void publishEvent(T inputMessage) {
        var notification = convert(inputMessage);
        eventPublisher.publishEvent(notification);
    }
}
