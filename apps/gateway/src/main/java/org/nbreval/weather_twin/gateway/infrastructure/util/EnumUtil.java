package org.nbreval.weather_twin.gateway.infrastructure.util;

import java.util.Optional;

/**
 * Some utility methods to interact with enum classes.
 */
public class EnumUtil {

  /**
   * Casts safely a string value as a specified enum class' name.
   * 
   * @param <E>       Type of enum to cast.
   * @param enumClass Class of enum type to cast.
   * @param name      Name of enum to cast.
   * @return An optional with cast string to expected enum type, or empty if it
   *         fails.
   */
  public static <E extends Enum<E>> Optional<E> safeValueOf(Class<E> enumClass, String name) {
    try {
      return Optional.of(Enum.valueOf(enumClass, name));
    } catch (IllegalArgumentException | NullPointerException e) {
      return Optional.empty();
    }
  }

}
