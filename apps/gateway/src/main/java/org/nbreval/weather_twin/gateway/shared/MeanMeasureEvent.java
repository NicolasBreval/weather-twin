package org.nbreval.weather_twin.gateway.shared;

public class MeanMeasureEvent extends SensorNotification {
    private final long windowSize;

    public MeanMeasureEvent(Object source, String deviceId, String sensorId, long utcTimestamp, double measure, long windowSize) {
        super(source, deviceId, sensorId, utcTimestamp, measure);
        this.windowSize = windowSize;
    }

    public long getWindowSize() {
        return windowSize;
    }
}
