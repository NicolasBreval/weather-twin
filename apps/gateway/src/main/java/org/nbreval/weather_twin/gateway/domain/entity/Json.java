package org.nbreval.weather_twin.gateway.domain.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a JSON in the system.
 */
public class Json {

  /**
   * Values inside the JSON.
   */
  private final Map<String, JsonValue<?>> values;

  public Json() {
    values = Map.of();
  }

  @SuppressWarnings("unchecked")
  public Json(Map.Entry<String, ? extends Object>... values) {
    this.values = Map.ofEntries(Arrays.stream(values)
        .map(v -> v.getValue() instanceof JsonValue<?> ? v : Map.entry(v.getKey(), new JsonValue<>(v.getValue())))
        .toArray(Map.Entry[]::new));
  }

  @SuppressWarnings("unchecked")
  public Json(Map<String, ? extends Object> values) {
    this.values = Map.ofEntries(values.entrySet().stream()
        .map(v -> v.getValue() instanceof JsonValue<?> ? v : Map.entry(v.getKey(), new JsonValue<>(v.getValue())))
        .toArray(Map.Entry[]::new));
  }

  /**
   * Adds new pair of key-value to this object.
   * 
   * @param key   Key to add.
   * @param value Value to add.
   * @return A new JSON with same content as current and the new pair key-value.
   */
  public Json add(String key, Object value) {
    var newMap = new HashMap<String, JsonValue<?>>();
    newMap.putAll(values);
    if (value instanceof JsonValue j) {
      newMap.put(key, j);
    } else {
      newMap.put(key, new JsonValue<>(value));
    }
    return new Json(newMap);
  }

  /**
   * Joins two JSON.
   * 
   * @param other Another JSON to concat with current.
   * @return A new JSON with joined content of current and another JSON.
   */
  public Json concat(Json other) {
    var newMap = new HashMap<String, JsonValue<?>>();
    newMap.putAll(values);
    newMap.putAll(other.values);
    return new Json(newMap);
  }

  /**
   * Obtains the value related to specified key.
   * 
   * @param key Key to find the related value.
   * @return Value related to specified key.
   */
  public Object get(String key) {
    return Optional.ofNullable(values.get(key)).map(JsonValue::value).orElse(null);
  }

  /**
   * Checks if the JSON contains an specified key.
   * 
   * @param key Key to check if exists.
   * @return True if key exists, else false.
   */
  public boolean contains(String key) {
    return values.containsKey(key);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    sb.append("{");

    var i = 0;
    var valuesCount = values.entrySet().size();
    for (var entry : values.entrySet()) {
      sb.append("\"");
      sb.append(entry.getKey());
      sb.append("\"");
      sb.append(":");
      sb.append(entry.getValue().toString());

      if (i < (valuesCount - 1))
        sb.append(",");

      i++;
    }

    sb.append("}");
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return 31 * (int) values.entrySet().stream().map(entry -> entry.getKey().hashCode() * entry.getValue().hashCode())
        .reduce((a, b) -> a * b).orElse(0);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Json j) {
      if (values.keySet().equals(j.values.keySet())) {
        return values.entrySet().stream()
            .allMatch(entry -> Objects.equals(entry.getValue(), j.values.get(entry.getKey())));
      }
    }

    return false;
  }

}
