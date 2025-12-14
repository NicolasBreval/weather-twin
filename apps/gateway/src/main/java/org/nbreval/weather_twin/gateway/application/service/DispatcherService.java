package org.nbreval.weather_twin.gateway.application.service;

import java.util.Map.Entry;

import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;

/**
 * Implementation of {@link DispatcherPort}.
 */
public class DispatcherService implements DispatcherPort {

  /**
   * Logger object to show some messages in application's log.
   */
  private static final Logger logger = LoggerFactory.getLogger(DispatcherService.class);

  /**
   * Allows to obtain registred aggregation expressions from database.
   */
  private final ExpressionsDbPort expressionsDB;

  /**
   * Allows to obtain registered aggregations from database.
   */
  private final AggregationsDbPort aggregationsDB;

  /**
   * Allows to process measures using an expression.
   */
  private final MeasureProcessorPort measureProcessor;

  public DispatcherService(
      ExpressionsDbPort expressionsDB,
      AggregationsDbPort aggregationsDb,
      MeasureProcessorPort measureProcessor) {
    this.expressionsDB = expressionsDB;
    this.aggregationsDB = aggregationsDb;
    this.measureProcessor = measureProcessor;
  }

  @Override
  public Flux<Entry<Long, Object>> consume(String device, String sensor, Object value) {
    // Get aggregations
    var aggregationsByInterval = aggregationsDB.getAggregations(device, sensor);

    if (aggregationsByInterval.isEmpty()) {
      logger.error("There are no aggregations registered for '%s.%s'".formatted(device, sensor));
    }

    // Get aggregation expression
    var expression = expressionsDB.getAggregatorExpression(device, sensor);

    return measureProcessor.aggregateMeasure(device, sensor, value, aggregationsByInterval, expression)
        .doOnNext(e -> {
          var interval = e.getKey();
          var aggregated = e.getValue();
          aggregationsDB.updateAggregation(device, sensor, interval, aggregated);
        })
        .doOnTerminate(() -> aggregationsDB.applyChanges());
  }

}
