package org.nbreval.weather_twin.gateway.application.port.out;

import java.util.Map;

/**
 * Defines the entity responsible for manage all aggregation expressions
 * and flush expressions used on system to aggregate measures and process
 * aggregations before send them.
 */
public interface ExpressionsDbPort {

  /**
   * Obtains an aggregation expression related to specified device and sensor.
   * 
   * @param device Device related to expression to find.
   * @param sensor Sensor related to expression to find.
   * @return The aggregation expression related to specified device and sensor.
   */
  String getAggregatorExpression(String device, String sensor);

  /**
   * Sets the aggregation expression related to specified device and sensor.
   * 
   * @param device     Device related to expression to set.
   * @param sensor     Sensor related to expression to set.
   * @param expression Expression to set.
   * @return New expression set on database for specified device and sensor.
   */
  String setAggregatorExpression(String device, String sensor, String expression);

  /**
   * Obtains all aggregation expressions stored in database.
   * 
   * @return All aggregation expressions from database, grouped by device and
   *         sensor.
   */
  Map<String, Map<String, String>> getAllAggregatorExpressions();

  /**
   * Removes an aggregation expression for the specified device and sensor.
   * 
   * @param device Device related to expression to remove.
   * @param sensor Sensor related to expression to remove.
   * @return Expression removed.
   */
  String removeAggregatorExpression(String device, String sensor);

  /**
   * Obtains a flush expression related to specified device and sensor.
   * 
   * @param device Device related to expression to find.
   * @param sensor Sensor related to expression to find.
   * @return The flush expression related to specified device and sensor.
   */
  String getFlushExpression(String device, String sensor);

  /**
   * Sets the flush expression related to specified device and sensor.
   * 
   * @param device     Device related to expression to set.
   * @param sensor     Sensor related to expression to set.
   * @param expression Expression to set.
   * @return New expression set on database for specified device and sensor.
   */
  String setFlushExpression(String device, String sensor, String expression);

  /**
   * Obtains all flush expressions stored in database.
   * 
   * @return All flush expressions from database, grouped by device and sensor.
   */
  Map<String, Map<String, String>> getAllFlushExpressions();

  /**
   * Removes a flush expression for the specified device and sensor.
   * 
   * @param device Device related to expression to remove.
   * @param sensor Sensor related to expression to remove.
   * @return Expression removed.
   */
  String removeFlushExpression(String device, String sensor);
}
