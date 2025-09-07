package org.nbreval.weather_twin.gateway.shared;

import org.springframework.context.ApplicationEvent;

/**
 * A notification representing a sensor measurement from a device.
 * This event is published whenever a new sensor measurement is received.
 */
public class SensorNotification extends ApplicationEvent {
    /** ID of the device from which the received message is sent */
    private final String deviceId;

    /** ID of the sensor from which the measure is taken and sent to the device */
    private final String sensorId;

    /** UTC timestamp when the measure was taken from sensor */
    private final long utcTimestamp;

    /** Measure taken from sensor, as a double value */
    private final double measure;

    /**
     * Constructs a new {@link SensorNotification} with the specified details.
     * @param source the object on which the event initially occurred (never null)
     * @param deviceId the ID of the device from which the message is sent
     * @param sensorId the ID of the sensor from which the measure is taken
     * @param utcTimestamp the UTC timestamp when the measure was taken
     * @param measure the measure taken from the sensor
     */
    public SensorNotification(Object source, String deviceId, String sensorId, long utcTimestamp, double measure) {
        super(source);
        this.deviceId = deviceId;
        this.sensorId = sensorId;
        this.utcTimestamp = utcTimestamp;
        this.measure = measure;
    }

    /**
     * Gets the ID of the device from which the message is sent.
     * @return the device ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Gets the ID of the sensor from which the measure is taken.
     * @return the sensor ID
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Gets the UTC timestamp when the measure was taken.
     * @return the UTC timestamp
     */
    public long getUtcTimestamp() {
        return utcTimestamp;
    }

    /**
     * Gets the measure taken from the sensor.
     * @return the sensor measure
     */
    public double getMeasure() {
        return measure;
    }
}
