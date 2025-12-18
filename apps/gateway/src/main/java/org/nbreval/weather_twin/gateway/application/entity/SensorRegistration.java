package org.nbreval.weather_twin.gateway.application.entity;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a new sensor to register on system.
 * 
 * @param device                The device name related to sensor.
 * @param sensor                The sensor to register.
 * @param aggregationExpression The WTAL expression to specify how to aggregate
 *                              sensor's measures.
 * @param flushExpression       The WTAL expression to specify how to flush
 *                              aggregated measures before to send it to
 *                              external systems.
 * @param defaultValue          The default value of measures, used as first
 *                              registered value and as value to set when system
 *                              flushes the aggregated measure.
 * @param intervals             List of intervals to register with the sensor.
 *                              Each interval registers a different aggregation
 *                              to bw managed by different schedulers.
 * @param sensorType            Type of sensor to register, like temperature,
 *                              humidity, pressure,...
 * @param magnitude             Magnitude referenced to sensor measures, like
 *                              kg, mL, dB,...
 * @param description           A description defined by user to identify
 *                              sensor.
 */
public record SensorRegistration(
    @NotBlank(message = "The device is mandatory, it must be not null or empty") String device,
    @NotBlank(message = "The sensor is mandatory, it must be not null or empty") String sensor,
    @NotBlank(message = "The aggregatorExpression is mandatory, it must be not null or empty") String aggregationExpression,
    String flushExpression,
    @NotNull(message = "The defaultValue is mandatory, and it must be parseable by dataType") String defaultValue,
    @NotEmpty(message = "The intervals is mandatory, it must be not null or empty") Set<Long> intervals,
    @NotNull(message = "The sensor type is mandatory, it must be not null") SensorType sensorType,
    String magnitude,
    String description) {

  public String[] getAggregationExpressionParts() {
    return getExpressionParts(aggregationExpression);
  }

  public String[] getFlushExpressionParts() {
    return getExpressionParts(flushExpression == null ? "" : flushExpression);
  }

  private String[] getExpressionParts(String expression) {
    var parts = new ArrayList<String>();
    var matcher = Pattern.compile("(\\w+|\".*\")").matcher(expression);

    var lastIndex = 0;
    while (matcher.find()) {
      if (matcher.start() > lastIndex) {
        parts.add(expression.substring(lastIndex, matcher.start()));
      }

      parts.add(matcher.group());

      lastIndex = matcher.end();
    }

    if (lastIndex < expression.length()) {
      parts.add(expression.substring(lastIndex));
    }

    return parts.toArray(new String[0]);
  }

}
