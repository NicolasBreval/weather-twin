package org.nbreval.weather_twin.gateway.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the required information to update an aggregated or flush
 * expression related to a sensor.
 * 
 * @param device     The device name related to the sensor.
 * @param sensor     The sensor to update its expression.
 * @param expression Expression to replace the old one.
 */
public record UpdateExpressionDto(
    @NotBlank(message = "The device is mandatory, it must be not null or empty") String device,
    @NotBlank(message = "The sensor is mandatory, it must be not null or empty") String sensor,
    @NotBlank(message = "The expression is mandatory, it must be not null or empty") String expression) {

}
