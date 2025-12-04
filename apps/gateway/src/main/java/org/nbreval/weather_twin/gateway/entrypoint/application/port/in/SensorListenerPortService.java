package org.nbreval.weather_twin.gateway.entrypoint.application.port.in;

import org.nbreval.weather_twin.gateway.shared.domain.model.SensorNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;

/**
 * An abstract port defining the contract for listening to sensor messages from
 * devices.
 * It uses Spring's event publishing mechanism to publish
 * {@link SensorNotification} events
 * when a new message is received. Concrete implementations must provide the
 * logic to convert
 * the input message type {@code T} into a {@link SensorNotification}.
 *
 * @param <T> The type of the input message that the listener will handle.
 */
public abstract class SensorListenerPortService<T> implements DisposableBean {
  protected static final Logger LOGGER = LoggerFactory.getLogger(SensorListenerPortService.class);

  /** Object used to publish Spring's events */
  protected final ApplicationEventPublisher eventPublisher;

  /**
   * Constructs a new {@link SensorListenerPortService} with the specified event
   * publisher.
   * 
   * @param eventPublisher the application event publisher used to publish sensor
   *                       notifications (injected by Spring)
   */
  public SensorListenerPortService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  /**
   * Transforms the input message of type {@code T} into a
   * {@link SensorNotification}.
   * Concrete implementations must provide the conversion logic.
   * 
   * @param inputMessage the input message to be converted
   * @return the corresponding {@link SensorNotification}
   */
  protected abstract SensorNotification convert(T inputMessage);

  /**
   * Converts and sends the input message as a Spring event.
   * 
   * @param inputMessage the input message to be published
   */
  protected void publishEvent(T inputMessage) {
    var notification = convert(inputMessage);
    LOGGER.debug("Sending notification from '%s/%s/%d' with measure '%f'".formatted(notification.getDeviceId(),
        notification.getSensorId(), notification.getUtcTimestamp(), notification.getMeasure()));
    eventPublisher.publishEvent(notification);
  }
}
