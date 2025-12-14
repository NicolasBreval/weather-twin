package org.nbreval.weather_twin.gateway.application.port.in;

import java.util.List;
import java.util.Set;

import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

import reactor.util.function.Tuple2;

/**
 * Defines the entity used to configure some aspects related to a sensor in the
 * system.
 * 
 * Allows to register the WTAL aggregation and flush expressions of a sensor,
 * unregister it, updates its expresions or intervals... Also, this entity
 * notifies if is required any scheduling or unscheduling, based on interval
 * operations.
 */
public interface SensorConfigurationPort {

  /**
   * Registers a new sensor on system.
   * 
   * @param device                Device related to sensor.
   * @param sensor                Sensor's name.
   * @param dataType              Data type expected for senor's measures.
   * @param defaultValue          Default value used when sensor's aggregation is
   *                              released.
   * @param aggregationExpression WTAL expression used to aggregate measures
   *                              received.
   * @param flushExpression       WTAL expression used to modify aggregation
   *                              before
   *                              to be sent to external systems.
   * @param intervals             List of intervals related to sensor. This
   *                              determines when the aggregated values are
   *                              flushed to external systems using schedulers.
   * 
   * @return The list of intervals which are not related to an scheduler on
   *         system.
   */
  Set<Long> registerSensor(String device, String sensor, DataType dataType, String defaultValue,
      String aggregationExpression, String flushExpression, Set<Long> intervals);

  /**
   * Unregisters a sensor from system.
   * 
   * @param device Device related to sensor.
   * @param sensor Sensor to unregister.
   * @return List of intervals to unschedule, because there are no more sensors
   *         related to them.
   */
  Set<Long> unregisterSensor(String device, String sensor);

  /**
   * Unregisters only an interval related to a sensor.
   * 
   * @param device   Device related to sensor.
   * @param sensor   Sensor to unregister its interval.
   * @param interval Interval related to sensor.
   * @return True if there are no more sensors related to the interval, so the
   *         scheduler related to interval needs to be unscheduled, else false.
   */
  boolean unregisterSensor(String device, String sensor, long interval);

  /**
   * Updates the WTAL aggregation expression related to sensor.
   * 
   * @param device     Device related to sensor.
   * @param sensor     Sensor to update its aggregation expression.
   * @param expression Expression to update.
   */
  void updateAggregationExpression(String device, String sensor, String expression);

  /**
   * Updates the WTAL flush expression related to sensor.
   * 
   * @param device     Device related to sensor.
   * @param sensor     Sensor to update its flush expression.
   * @param expression Expression to update.
   */
  void updateFlushExpression(String device, String sensor, String expression);

  /**
   * Updates the intervals related to sensor. The input list defines the intervals
   * that will persists after the method is applied. So, the previous intervals
   * not included in input list will be removed.
   * 
   * @param device    Device related to sensor.
   * @param sensor    Sensor to update its intervals.
   * @param intervals List of intervals to update.
   * @return A tuple with two lists of intervals; first one is the list of
   *         intervals which requires to remove the schedulers related to them,
   *         and the second one are the intervals which requires to create new
   *         schedulers for them.
   */
  Tuple2<List<Long>, List<Long>> updateIntervals(String device, String sensor, Set<Long> intervals);
}
