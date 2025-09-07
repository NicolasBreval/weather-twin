package org.nbreval.weather_twin.gateway.entrypoint.port.in;

import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;

public abstract class SensorListenerPort<T> implements DisposableBean {

    protected final ApplicationEventPublisher eventPublisher;

    public SensorListenerPort(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected abstract SensorNotification convert(T inputMessage);

    protected void publishEvent(T inputMessage) {
        var notification = convert(inputMessage);
        eventPublisher.publishEvent(notification);
    }
}
