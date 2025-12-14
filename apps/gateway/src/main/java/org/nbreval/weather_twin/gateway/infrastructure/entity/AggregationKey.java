package org.nbreval.weather_twin.gateway.infrastructure.entity;

/**
 * Entity used to group aggregations in database.
 * 
 * @param device   The device name related to the aggregation.
 * @param sensor   The sensor name related to the aggregation.
 * @param interval The interval related to the aggregation.
 */
public record AggregationKey(
    String device,
    String sensor,
    long interval) {

}
