package org.nbreval.weather_twin.gateway.application.entity;

import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;

public record SensorMetadata(
    String device,
    String sensor,
    SensorType type,
    String magnitude,
    String description) {

}
