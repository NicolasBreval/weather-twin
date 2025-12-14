package org.nbreval.weather_twin.gateway.application.port.in;

import java.util.Map;

import reactor.core.publisher.Flux;

/**
 * Defines the entity reposible for receiving measures from external channels
 * and process them.
 * 
 * The dispatcher takes the received measure and uses it to execute the
 * registered aggregation
 * expression for the measure's device and sensor, for each interval registered
 * too.
 */
public interface DispatcherPort {

  /**
   * Consumes a new measure from an external sensor.
   * 
   * @param device Device which sends the measure.
   * @param sensor Sensor which sends the measure.
   * @param value  Value of the received measure.
   * @return The result of aggregate received value for each registered interval.
   */
  Flux<Map.Entry<Long, Object>> consume(String device, String sensor, Object value);
}
