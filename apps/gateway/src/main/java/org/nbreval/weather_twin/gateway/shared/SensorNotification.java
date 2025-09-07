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

    public SensorNotification(Object source, String deviceId, String sensorId, long utcTimestamp, double measure) {
        super(source);
        this.deviceId = deviceId;
        this.sensorId = sensorId;
        this.utcTimestamp = utcTimestamp;
        this.measure = measure;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public long getUtcTimestamp() {
        return utcTimestamp;
    }

    public double getMeasure() {
        return measure;
    }
}
