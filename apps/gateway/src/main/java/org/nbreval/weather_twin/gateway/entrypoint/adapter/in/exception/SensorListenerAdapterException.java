package org.nbreval.weather_twin.gateway.entrypoint.adapter.in.exception;

/**
 * Custom exception thrown when there is any error in a SensorListenerAdapter
 */
public class SensorListenerAdapterException extends RuntimeException {

    public SensorListenerAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
