package org.nbreval.weather_twin.gateway.domain.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents an array on system.
 */
public class JsonArray {

  /**
   * Values stored on array.
   */
  private final List<JsonValue<?>> values;

  public JsonArray() {
    this.values = List.of();
  }

  public JsonArray(List<? extends Object> values) {
    this.values = values.stream().map(v -> v instanceof JsonValue<?> j ? j : new JsonValue<>(v)).toList();
  }

  public JsonArray(Object... values) {
    this.values = Arrays.stream(values).map(v -> v instanceof JsonValue<?> j ? j : new JsonValue<>(v)).toList();
  }

  /**
   * Obtains the element stored in the specified position. If position is
   * negative, returns the element in the position equivalent to substract the
   * negative value to array's size. If position is greater than array size,
   * returns null. Examples:
   * 
   * <pre>{@code
   * var array = new JsonArray(1, 2, 3, 4, 5);
   * IO.println(array.get(1)); // Prints 2
   * IO.println(array.get(-1)); // Prints 5
   * IO.println(array.get(-3)); // Prints 3
   * IO.println(array.get(5)); // Prints null
   * IO.println(array.get(-6)); // Prints null
   * }</pre>
   * 
   * @param i Position of element to obtain.
   * @return The element to obtain, or null if index is outside array limits.
   * 
   */
  public Object get(int i) {
    var fixed = fixIndex(i);
    return fixed < 0 || fixed >= values.size() ? null : values.get(fixed).value();
  }

  /**
   * Obtains the number of elements in array.
   * 
   * @return The number of elements in array.
   */
  public int size() {
    return values.size();
  }

  /**
   * Returns a subset of elements of the array, delimited by the specified
   * indexes, left inclusive and right exclusive. If any index is less than zero,
   * is transformed than the equivalent
   * to substract the array size and this negative index. If indexes are outside
   * array limits, returns an empty array. Examples:
   * 
   * <pre>{@code
   * var array = new JsonArray(1, 2, 3, 4, 5);
   * IO.println(array.get(1, 3)); // Prints [2, 3]
   * IO.println(array.get(3, 6)); // Prints [4, 5, null]
   * IO.println(array.get(-2, -1)); // Prints [2, 3, 4]
   * }</pre>
   * 
   * @param from Start index of array's subset.
   * @param to   End index of array's subset.
   * @return New array with copy of elements delimited by specified indexes.
   * @throws IllegalArgumentException If 'from' is greater than 'to'.
   */
  public JsonArray get(int from, int to) {
    var fixedFrom = fixIndex(from);
    var fixedTo = fixIndex(to);

    if (fixedFrom > fixedTo) {
      throw new IllegalArgumentException("Invalid index range '[%d, %d)'".formatted(fixedFrom, fixedTo));
    }

    var newList = new ArrayList<JsonValue<?>>();

    for (var i = fixedFrom; i < fixedTo; i++) {
      newList.add(i > values.size() ? new JsonValue<>(null) : values.get(i));
    }

    return new JsonArray(newList);
  }

  /**
   * Adds new value in array.
   * 
   * @param value Value to add.
   * @return New array with all elements inside current array and new element
   *         added.
   */
  public JsonArray add(JsonValue<?> value) {
    return new JsonArray(Stream.concat(values.stream(), Stream.of(value)).toList());
  }

  /**
   * Adds new value in array.
   * 
   * @param value Value to add.
   * @return New array with all elements inside current array and new element
   *         added.
   */
  public JsonArray add(Object value) {
    return add(new JsonValue<>(value));
  }

  /**
   * Concats current and other array into new one.
   * 
   * @param other Another array to concat with current.
   * @return New array as result of concat current array and other.
   */
  public JsonArray concat(JsonArray other) {
    return new JsonArray(Stream.concat(values.stream(), other.values.stream()).toList());
  }

  /**
   * Checks if a value is present in current array.
   * 
   * @param value Value to check.
   * @return True if value is inside array, else false.
   */
  public boolean contains(Object value) {
    return values.contains(value instanceof JsonValue<?> j ? j : new JsonValue<>(value));
  }

  /**
   * Applies a filter function over current array.
   * 
   * @param lambda Filter function to apply over each array item.
   * @return New array filtered by lambda function.
   */
  public JsonArray filter(Function<Object, Object> lambda) {
    var newArray = new ArrayList<JsonValue<?>>();

    for (var i = 0; i < values.size(); i++) {
      var value = values.get(i);
      var result = lambda.apply(value.value());

      if (result instanceof Boolean b) {
        if (b)
          newArray.add(value);
      } else {
        throw new IllegalArgumentException("Lambda function doesn't return a valid boolean result");
      }
    }

    return new JsonArray(newArray);
  }

  /**
   * Applies a map function over current array.
   * 
   * @param lambda Map function to apply over each array item.
   * @return New array mapped by lambda function.
   */
  public JsonArray map(Function<Object, Object> lambda) {
    return new JsonArray(values.stream().map(v -> {
      var newValue = lambda.apply(v.value());
      return newValue instanceof JsonValue<?> j ? j : new JsonValue<>(newValue);
    }).toList());
  }

  private int fixIndex(int index) {
    return index < 0 ? values.size() + index : index;
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();

    sb.append("[");

    for (var i = 0; i < values.size(); i++) {
      var value = values.get(i);

      sb.append(value);

      if (i < values.size() - 1) {
        sb.append(",");
      }
    }

    sb.append("]");

    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JsonArray a) {
      return values.equals(a.values);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 31 * (int) values.stream().map(Object::hashCode).reduce((a, b) -> a * b).orElse(0);
  }
}
