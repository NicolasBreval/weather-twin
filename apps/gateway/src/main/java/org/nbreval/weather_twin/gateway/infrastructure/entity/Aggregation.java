package org.nbreval.weather_twin.gateway.infrastructure.entity;

import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;

/**
 * Represenst the result of aggregate multiple measures received for a specified
 * sensor.
 * 
 * @param value        The value aggregated by system.
 * @param defaultValue The value to use when aggregation is released.
 * @param steps        Number of aggregations applied, starts from 1 and the
 *                     initial default value counts as step.
 */
public record Aggregation(
    Object value,
    Object defaultValue,
    int steps) {
  public Aggregation(Object value, Object defaultValue, int steps) {
    if (value != null
        && !(value instanceof Float || value instanceof Integer || value instanceof Boolean || value instanceof String
            || value instanceof Json || value instanceof JsonArray)) {
      throw new IllegalArgumentException(
          "Type '%s' for value is not valid".formatted(value.getClass().getSimpleName()));
    }

    if (defaultValue == null) {
      throw new IllegalArgumentException("Default value can't be null");
    }

    if (steps < 1) {
      throw new IllegalArgumentException("Invalid number of steps, it must be greater than zero");
    }

    this.value = value;
    this.defaultValue = defaultValue;
    this.steps = steps;
  }
}
