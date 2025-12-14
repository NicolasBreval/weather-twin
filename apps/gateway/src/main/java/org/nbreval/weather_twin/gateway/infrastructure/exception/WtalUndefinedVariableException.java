package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception thrown when an expression tries to use an undefined variable.
 */
public class WtalUndefinedVariableException extends RuntimeException {
  public WtalUndefinedVariableException(String message) {
    super(message);
  }

}
