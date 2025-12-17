package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nbreval.weather_twin.gateway.application.entity.Aggregation;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.infrastructure.util.AggregatorWtalEvaluator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implements a {@MeasureProcessorPort}.
 * 
 * This adapter is the responsible for process all measures received and process
 * the aggregated measures before to be sent to external systems, using an
 * expression based in a WTAL.
 */
public class MeasureProcessorAdapter implements MeasureProcessorPort {

  /**
   * Contains all logic to perform operations described in the WTAL.
   */
  private final WTALLogicPort wtalLogic;

  public MeasureProcessorAdapter(WTALLogicPort wtalLogic) {
    this.wtalLogic = wtalLogic;
  }

  @Override
  public Flux<Map.Entry<Long, Object>> aggregateMeasure(String device, String sensor, Object value,
      Map<Long, Aggregation> aggregationsByInterval,
      String aggregationExpression) {

    return Flux.fromIterable(aggregationsByInterval.entrySet())
        .map(entry -> {
          var aggregated = entry.getValue();

          var context = new HashMap<String, Object>();
          context.put("curr", value);
          context.put("agg", aggregated.value());
          context.put("steps", aggregated.steps());

          var evaluator = new AggregatorWtalEvaluator(wtalLogic, context);
          return Map.entry(entry.getKey(), evaluator.evaluate(aggregationExpression));
        });
  }

  @Override
  public Mono<Entry<Long, Object>> flushMeasure(String device, String sensor, long interval, Aggregation aggregation,
      String fluxExpression) {

    return Mono.fromCallable(() -> {
      var context = new HashMap<String, Object>();
      context.put("agg", aggregation.value());
      context.put("steps", aggregation.steps());
      context.put("interval", interval);

      var evaluator = new AggregatorWtalEvaluator(wtalLogic, context);
      return Map.entry(interval, evaluator.evaluate(fluxExpression));
    });
  }

}
