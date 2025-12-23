package org.nbreval.weather_twin.gateway.application.port.in;

import java.util.Collection;
import java.util.Set;

import org.nbreval.weather_twin.gateway.application.entity.SensorRegistration;
import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;

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
   * @param sensorType            Type of sensor to register, like temperature,
   *                              humidity, pressure,...
   * @param magnitude             Magnitude referenced to sensor measures, like
   *                              kg, mL, dB,...
   * @param description           A description defined by user to identify
   *                              sensor.
   * 
   * @return The list of intervals which are not related to an scheduler on
   *         system.
   */
  Set<Long> registerSensor(String device, String sensor, String defaultValue,
      String aggregationExpression, String flushExpression, Set<Long> intervals, SensorType sensorType,
      String magnitude, String description);

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
   * Updates some information about a registered sensor
   * 
   * @param device                Device of sensor to update.
   * @param sensor                Name of sensor to update.
   * @param aggregationExpression New aggregation expression.
   * @param flushExpression       New flushExpression.
   * @param defaultValue          New default value.
   * @param intervals             New set of intervals.
   * @param sensorType            New sensor type.
   * @param magnitude             New magnitude.
   * @param description           New description.
   */
  void updateSensor(String device, String sensor, String aggregationExpression, String flushExpression,
      String defaultValue, Set<Long> intervals, SensorType sensorType, String magnitude, String description);

  /**
   * Obtains all aggregations stored on system.
   * 
   * @return All aggregations stored on system grouped by interval, device and
   *         sensor
   */
  Collection<SensorRegistration> getAllRegistrations();

  /**
   * Obtain a sensor registratio by its name and device.
   * 
   * @param device Device related to sensor registration to obtain.
   * @param sensor Sensor to obtain.
   * @return Sensor registration related by device and sensor passed by
   *         parameters.
   */
  SensorRegistration getSensorRegistration(String device, String sensor);
}
