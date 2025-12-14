package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception thrown on an update of entity which doesn'r exist.
 */
public class NotExistsException extends RuntimeException {
  public NotExistsException(String message) {
    super(message);
  }
}
