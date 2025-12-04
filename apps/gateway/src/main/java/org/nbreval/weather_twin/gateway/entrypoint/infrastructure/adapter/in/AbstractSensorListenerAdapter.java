package org.nbreval.weather_twin.gateway.entrypoint.infrastructure.adapter.in;

import java.util.Objects;

import org.nbreval.weather_twin.gateway.entrypoint.application.port.in.SensorListenerPort;
import org.nbreval.weather_twin.gateway.shared.domain.model.SensorNotification;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractSensorListenerAdapter<T> implements SensorListenerPort<T> {

  private final ApplicationEventPublisher eventPublisher;

  public AbstractSensorListenerAdapter(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  protected abstract SensorNotification convert(T inputMessage);

  @Override
  public void publishEvent(T inputMessage) {
    var converted = convert(inputMessage);

    if (Objects.nonNull(converted))
      eventPublisher.publishEvent(converted);
  }

}
