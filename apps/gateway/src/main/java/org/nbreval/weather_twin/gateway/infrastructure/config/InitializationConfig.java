package org.nbreval.weather_twin.gateway.infrastructure.config;

import java.util.Set;

import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.in.SchedulerPort;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.service.DispatcherService;
import org.nbreval.weather_twin.gateway.application.service.SchedulerService;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializationConfig {

  @Bean
  public ExpressionsDbPort expressionsDB(
      @Value("${db.aggregators.location}") String aggregatorsLocation,
      @Value("${db.flushes.location}") String flushesLocation) {
    return new ExpressionsDbAdapter(aggregatorsLocation, flushesLocation);
  }

  @Bean
  public AggregationsDbPort aggregationsDB(
      @Value("${db.aggregations.location}") String aggregationsLocation) {
    return new AggregationsDbAdapter(aggregationsLocation);
  }

  @Bean
  public WTALLogicPort wtalLogic() {
    return new WtalLogicService();
  }

  @Bean
  public MeasureProcessorPort measureProcessor(WTALLogicPort wtalLogic) {
    return new MeasureProcessorAdapter(wtalLogic);
  }

  @Bean
  public DispatcherPort dispatcher(ExpressionsDbPort expressionsDB, AggregationsDbPort aggregationsDB,
      MeasureProcessorPort measureProcessor) {
    return new DispatcherService(expressionsDB, aggregationsDB, measureProcessor);
  }

  @Bean
  public SchedulerPort scheduler(AggregationsDbPort aggregationsDB, ExpressionsDbPort expressionsDB,
      MeasureProcessorPort measureProcessor) {
    // TODO: Output connectors
    return new SchedulerService(aggregationsDB, expressionsDB, measureProcessor, Set.of());
  }
}
