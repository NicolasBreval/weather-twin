package org.nbreval.weather_twin.gateway.infrastructure.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the required information to modify the set of intervals related to
 * a sensor.
 * 
 * @param device    The device name related to a sensor.
 * @param sensor    The sensor to update its intervals.
 * @param intervals The set of intervals to replace the old ones.
 */
public record UpdateIntervalsDto(
    @NotBlank(message = "The device is mandatory, it must be not null or empty") String device,
    @NotBlank(message = "The sensor is mandatory, it must be not null or empty") String sensor,
    @NotEmpty(message = "The intervals is mandatory, it must be not null or empty") Set<Long> intervals) {

}
