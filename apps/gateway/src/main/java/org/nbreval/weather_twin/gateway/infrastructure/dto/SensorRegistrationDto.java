package org.nbreval.weather_twin.gateway.infrastructure.dto;

import java.util.Set;

import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a new sensor to register on system.
 * 
 * @param device                The device name related to sensor.
 * @param sensor                The sensor to register.
 * @param aggregationExpression The WTAL expression to specify how to aggregate
 *                              sensor's measures.
 * @param flushExpression       The WTAL expression to specify how to flush
 *                              aggregated measures before to send it to
 *                              external systems.
 * @param dataType              The type of data to expect in received measures,
 *                              is used to check and transform received data.
 * @param defaultValue          The default value of measures, used as first
 *                              registered value and as value to set when system
 *                              flushes the aggregated measure.
 * @param intervals             List of intervals to register with the sensor.
 *                              Each interval registers a different aggregation
 *                              to bw managed by different schedulers.
 */
public record SensorRegistrationDto(
    @NotBlank(message = "The device is mandatory, it must be not null or empty") String device,
    @NotBlank(message = "The sensor is mandatory, it must be not null or empty") String sensor,
    @NotBlank(message = "The aggregatorExpression is mandatory, it must be not null or empty") String aggregationExpression,
    String flushExpression,
    @NotNull(message = "The dataType is mandatory") DataType dataType,
    @NotNull(message = "The defaultValue is mandatory, and it must be parseable by dataType") String defaultValue,
    @NotEmpty(message = "The intervals is mandatory, it must be not null or empty") Set<Long> intervals) {
}
