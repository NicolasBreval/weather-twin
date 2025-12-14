package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception thrown when a expression has an invalid operator.
 */
public class WtalIllegalOperationException extends RuntimeException {
  public WtalIllegalOperationException(String message) {
    super(message);
  }

  public WtalIllegalOperationException(Throwable t) {
    super(t);
  }
}
