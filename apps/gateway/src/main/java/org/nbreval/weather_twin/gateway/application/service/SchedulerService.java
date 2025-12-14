package org.nbreval.weather_twin.gateway.application.service;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.nbreval.weather_twin.gateway.application.port.in.SchedulerPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.port.out.OutputConnectorPort;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

/**
 * Implements a {@link SchedulerPort}.
 */
public class SchedulerService implements SchedulerPort {

  /**
   * List of enabled scheduled tasks.
   */
  private final Map<Long, Disposable> schedulings;

  /**
   * Allows to obtain registered flush expressions from database.
   */
  private final ExpressionsDbPort expressionsDB;

  /**
   * Allows to obtain aggregated values from database.
   */
  private final AggregationsDbPort aggregationsDB;

  /**
   * Allows to process a flush expression with an aggregated value.
   */
  private final MeasureProcessorPort measureProcessor;

  /**
   * List of output connectors to use to send flush results to external systems.
   */
  private final Set<OutputConnectorPort> outputs;

  public SchedulerService(AggregationsDbPort aggregationsDB, ExpressionsDbPort expressionsDB,
      MeasureProcessorPort measureProcessor, Set<OutputConnectorPort> outputs) {
    this.schedulings = new ConcurrentHashMap<>();
    this.aggregationsDB = aggregationsDB;
    this.expressionsDB = expressionsDB;
    this.measureProcessor = measureProcessor;
    this.outputs = outputs;
  }

  @Override
  public void schedule(long interval) {
    schedulings.computeIfAbsent(interval, _ -> Flux.interval(Duration.ofMillis(interval)).flatMap(_ -> {
      return Flux.fromStream(aggregationsDB.getAggregationsByInterval(interval).entrySet().stream()
          .flatMap(deviceEntry -> deviceEntry.getValue().entrySet().stream().map(sensorEntry -> {
            var flushExpression = expressionsDB.getFlushExpression(deviceEntry.getKey(), sensorEntry.getKey());

            if (flushExpression == null)
              return Mono
                  .just(
                      Tuples.of(deviceEntry.getKey(), sensorEntry.getKey(), interval, sensorEntry.getValue().value()));

            return measureProcessor.flushMeasure(deviceEntry.getKey(), sensorEntry.getKey(), interval,
                sensorEntry.getValue(), flushExpression)
                .map(result -> Tuples.of(deviceEntry.getKey(), sensorEntry.getKey(), result.getKey(),
                    result.getValue()));
          }))).flatMap(Function.identity());
    })
        .doOnNext(entry -> {
          var device = entry.getT1();
          var sensor = entry.getT2();
          var interv = entry.getT3();
          var result = entry.getT4();
          aggregationsDB.releaseAggregation(device, sensor, interv);
          aggregationsDB.applyChanges();
          outputs.forEach(output -> output.sendFlush(device, sensor, interval, result));
        })
        .subscribe());
  }

  @Override
  public void unschedule(long interval) {
    var scheduling = schedulings.remove(interval);
    scheduling.dispose();
  }

}
