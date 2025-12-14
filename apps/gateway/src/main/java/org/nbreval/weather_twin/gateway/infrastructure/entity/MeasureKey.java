package org.nbreval.weather_twin.gateway.infrastructure.entity;

/**
 * Entity to group measures by device and sensor.
 * 
 * @param device The device name related to a measure.
 * @param sensor The sensor name related to a mesure.
 */
public record MeasureKey(
    String device,
    String sensor) {

}
