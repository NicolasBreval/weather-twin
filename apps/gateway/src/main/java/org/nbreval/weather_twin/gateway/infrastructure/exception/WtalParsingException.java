package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception thrown when an expression has syntax errors.
 */
public class WtalParsingException extends RuntimeException {
  public WtalParsingException(String message) {
    super(message);
  }

}
