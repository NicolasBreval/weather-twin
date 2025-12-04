package org.nbreval.weather_twin.gateway.entrypoint.application.port.in;

import org.springframework.beans.factory.DisposableBean;

/**
 * Port defining the contract for listening to sensor messages from devices.
 *
 * @param <T> The type of the input message that the listener will handle.
 */
public interface SensorListenerPort<T> extends DisposableBean {

  /**
   * Publishes an event based on the input message.
   * 
   * @param inputMessage the input message to be published
   */
  void publishEvent(T inputMessage);

}
