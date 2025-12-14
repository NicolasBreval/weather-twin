package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception to represents a failed insert operation if the entity to insert
 * already exists.
 */
public class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(String message) {
    super(message);
  }
}
