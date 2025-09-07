package org.nbreval.weather_twin.gateway.entrypoint.adapter.in.exception;

/**
 * Custom exception thrown when there is any error in a SensorListenerAdapter
 */
public class SensorListenerAdapterException extends RuntimeException {

    /**
     * Constructs a new SensorListenerAdapterException with the specified detail message.
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public SensorListenerAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
