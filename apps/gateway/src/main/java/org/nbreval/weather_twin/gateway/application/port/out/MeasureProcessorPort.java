package org.nbreval.weather_twin.gateway.application.port.out;

import java.util.Map;

import org.nbreval.weather_twin.gateway.application.entity.Aggregation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the entity responsible for process all measures received by system
 * and process the aggregated value when scheduler sends it to external systems.
 */
public interface MeasureProcessorPort {

  /**
   * Runs the aggregated expression for a specified device and sensor, with the
   * received measure's value, for each registered interval.
   * 
   * @param device                 Device related to aggregation expression to
   *                               execute.
   * @param sensor                 Sensor related to aggregation expression to
   *                               execute.
   * @param value                  Measure's value to aggregate
   * @param aggregationsByInterval All aggregations related to device and sensor,
   *                               grouped by interval.
   * @param aggregationExpression  Aggregation expression to run over measure's
   *                               expression.
   * @return All aggregations executed for each registered interval.
   */
  Flux<Map.Entry<Long, Object>> aggregateMeasure(String device, String sensor, Object value,
      Map<Long, Aggregation> aggregationsByInterval,
      String aggregationExpression);

  /**
   * Runs the flush expression for a specified device and sensor, with the related
   * aggregated value, for a specified interval.
   * 
   * @param device         Device related to flush expression to execute.
   * @param sensor         Sensor related to flush expression to execute.
   * @param interval       Interval related to schedule task which invokes this
   *                       method.
   * @param aggregation    Aggregated value related to device, sensor and
   *                       interval.
   * @param fluxExpression Expression to run over aggregated value.
   * @return Flush result with the related interval.
   */
  Mono<Map.Entry<Long, Object>> flushMeasure(String device, String sensor, long interval, Aggregation aggregation,
      String fluxExpression);

}
