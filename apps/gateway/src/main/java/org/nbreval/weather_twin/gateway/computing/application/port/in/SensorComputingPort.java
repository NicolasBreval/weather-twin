package org.nbreval.weather_twin.gateway.computing.application.port.in;

import org.nbreval.weather_twin.gateway.shared.domain.model.SensorNotification;
import org.springframework.beans.factory.DisposableBean;

/**
 * Port defining the contract for processing sensor notifications for
 * computing purposes.
 */
public interface SensorComputingPort extends DisposableBean {

  /**
   * Processes the given sensor notification for computing purposes.
   * 
   * @param notification the sensor notification to be processed
   */
  void processSensorNotification(SensorNotification notification);
}
