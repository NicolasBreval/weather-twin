package org.nbreval.weather_twin.gateway.shared;

import org.springframework.context.ApplicationEvent;

public class SensorNotification extends ApplicationEvent {
    private final String deviceId;
    private final String sensorId;
    private final long utcTimestamp;
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
