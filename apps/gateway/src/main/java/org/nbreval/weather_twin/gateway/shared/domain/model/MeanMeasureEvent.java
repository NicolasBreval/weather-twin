package org.nbreval.weather_twin.gateway.shared.domain.model;

/**
 * Event representing the mean measure over a specified time window from a
 * sensor.
 */
public class MeanMeasureEvent extends SensorNotification {
  /**
   * Size of the time window (in milliseconds) over which the mean measure is
   * calculated.
   */
  private final long windowSize;

  public MeanMeasureEvent(Object source, String deviceId, String sensorId, long utcTimestamp, double measure,
      long windowSize) {
    super(source, deviceId, sensorId, utcTimestamp, measure);
    this.windowSize = windowSize;
  }

  /**
   * Gets the size of the time window.
   * 
   * @return the window size in milliseconds
   */
  public long getWindowSize() {
    return windowSize;
  }
}
