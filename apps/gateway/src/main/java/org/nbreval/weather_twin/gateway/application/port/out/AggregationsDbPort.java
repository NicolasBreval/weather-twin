package org.nbreval.weather_twin.gateway.application.port.out;

import java.util.Map;
import java.util.Set;

import org.nbreval.weather_twin.gateway.application.entity.Aggregation;
import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

/**
 * Defines the entity which interacts with a MapDB database to store the
 * aggregated values of all registered sensors.
 * 
 * Each measure received by the gateway is aggregated and stored in MapDB, to
 * reduce the memory usage, and this
 * entity performs the communication with MapDB and OS's diks system.
 */
public interface AggregationsDbPort {

  /**
   * Registers new measure's aggregation in the database.
   * 
   * @param device       Device related to measure's aggregation.
   * @param sensor       Sensor related to measure's aggregation.
   * @param interval     Interval related to aggregation.
   * @param dataType     Expected data type of measures received by this
   *                     aggregation
   * @param defaultValue Default value to store when an aggregation is released.
   */
  void registerAggregation(String device, String sensor, long interval, DataType dataType, Object defaultValue);

  /**
   * Sets the value of already registered aggregation.
   * 
   * @param device      Device related to measure's aggregation to update.
   * @param sensor      Sensor related to measure's aggregation to update.
   * @param interval    Interval related aggregation to update.
   * @param aggregation New value to set in aggregation.
   */
  void updateAggregation(String device, String sensor, long interval, Object aggregation);

  /**
   * Returns an aggregation to the default value.
   * 
   * @param device   Device related to aggregation to release.
   * @param sensor   Sensor related to aggregation to release.
   * @param interval Interval related to aggregation to release.
   * @return Aggregation released.
   */
  Aggregation releaseAggregation(String device, String sensor, long interval);

  /**
   * Removes an aggregation from database.
   * 
   * @param device   Device related to aggregation to unregister.
   * @param sensor   Sensor related to aggregation to unregister.
   * @param interval Interval related to aggregation to unregister.
   */
  void unregisterAggregation(String device, String sensor, long interval);

  /**
   * Obtains all aggregations stored for specified device and sensor, grouped by
   * interval.
   * 
   * @param device Device related to aggregations.
   * @param sensor Sensor related to aggregations.
   * @return All aggregations related to device and sensor, grouped by interval.
   */
  Map<Long, Aggregation> getAggregations(String device, String sensor);

  /**
   * Runs a commit operation over database to store all in-memory changes into
   * disk.
   */
  void applyChanges();

  /**
   * Obtains all aggregations stored in database.
   * 
   * @return All aggregations, grouped by interval, device and sensor.
   */
  Map<Long, Map<String, Map<String, Aggregation>>> getAllAgregations();

  /**
   * Obtains all aggregations for a specified interval.
   * 
   * @param interval Interval to filter aggregations to find.
   * @return All aggregations for specified interval, grouped by device and
   *         sensor.
   */
  Map<String, Map<String, Aggregation>> getAggregationsByInterval(long interval);

  /**
   * Obtains all intervals registered on database.
   * 
   * @return All intervals registered on database.
   */
  Set<Long> getAllIntervals();

  /**
   * Otains all registered intervals for an specified device and sensor.
   * 
   * @param device Device related to intervals to find.
   * @param sensor Sensor related to intervals to find.
   * @return All registered intervals related to device and sensor.
   */
  Set<Long> getRegisteredIntervals(String device, String sensor);

  /**
   * Obtains an aggregation related to specified device, sensor and interval.
   * 
   * @param device   Device related to aggregation to find.
   * @param sensor   Sensor related to aggregation to find.
   * @param interval Interval related to aggregation to find.
   * @return The aggregation related to specified device, sensor and interval.
   */
  Aggregation getAggregation(String device, String sensor, long interval);

  /**
   * Closes the database completely
   */
  void close();
}
