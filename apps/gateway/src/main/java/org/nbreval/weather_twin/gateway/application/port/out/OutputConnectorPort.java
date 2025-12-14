package org.nbreval.weather_twin.gateway.application.port.out;

/**
 * Defines an entity used to send a flush result to an external system.
 */
public interface OutputConnectorPort {

  /**
   * Sends a flush result to an external system (database, another service,...).
   * 
   * @param device   Device related to flush value to send.
   * @param sensor   Sensor related to flush value to send.
   * @param interval Interval related to flush value to send.
   * @param value    Flush value to send.
   */
  void sendFlush(String device, String sensor, long interval, Object value);

}
