package org.nbreval.weather_twin.gateway.infrastructure.exception;

/**
 * Exception used when any problem prevents the application initialization.
 */
public class GatewayInitializationException extends RuntimeException {
  public GatewayInitializationException(String message) {
    super(message);
  }
}
