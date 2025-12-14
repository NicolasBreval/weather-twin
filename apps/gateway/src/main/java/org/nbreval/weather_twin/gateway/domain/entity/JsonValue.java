package org.nbreval.weather_twin.gateway.domain.entity;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Represents a value inside a {@link Json} or an {@link JsonArray}. This object
 * simply defines a type restriction for {@link Json} and {@link JsonArray}.
 */
public record JsonValue<T>(
    T value) {
  public JsonValue {
    if (value != null &&
        !(value instanceof String ||
            value instanceof Integer ||
            value instanceof Float ||
            value instanceof Boolean ||
            value instanceof JsonArray ||
            value instanceof Json)) {
      throw new IllegalArgumentException("Illegal value for JsonValue");
    }
  }

  @Override
  public String toString() {
    if (value instanceof String s) {
      return "\"" + s + "\"";
    } else if (value instanceof Number n) {
      return DecimalFormat.getInstance(Locale.US).format(n);
    }

    return value.toString();
  }

}
