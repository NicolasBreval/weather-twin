package org.nbreval.weather_twin.gateway.infrastructure.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.application.port.in.SchedulerPort;
import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.MeasureProcessorPort;
import org.nbreval.weather_twin.gateway.application.port.out.SensorMetadataDbPort;
import org.nbreval.weather_twin.gateway.application.service.DispatcherService;
import org.nbreval.weather_twin.gateway.application.service.SchedulerService;
import org.nbreval.weather_twin.gateway.application.service.SensorConfigurationService;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.AggregationsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.CoapAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.ExpressionsDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.MeasureProcessorAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.SensorMetadataDbAdapter;
import org.nbreval.weather_twin.gateway.infrastructure.exception.GatewayInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializationConfig {

  @Bean(destroyMethod = "close")
  public ExpressionsDbPort expressionsDB(
      @Value("${db.aggregators.location}") String aggregatorsLocation,
      @Value("${db.flushes.location}") String flushesLocation) {
    var aggregatorsParentFolder = Paths.get(aggregatorsLocation).getParent();

    if (!Files.exists(aggregatorsParentFolder)) {
      try {
        Files.createDirectories(aggregatorsParentFolder);
      } catch (IOException e) {
        throw new GatewayInitializationException(
            "Error creating parent folder for aggregators location '%s'".formatted(aggregatorsLocation));
      }
    }

    var flushesParentFolder = Paths.get(flushesLocation).getParent();

    if (!Files.exists(flushesParentFolder)) {
      try {
        Files.createDirectories(flushesParentFolder);
      } catch (IOException e) {
        throw new GatewayInitializationException(
            "Error creating parent folder for flushes location '%s'".formatted(flushesLocation));
      }
    }

    return new ExpressionsDbAdapter(aggregatorsLocation, flushesLocation);
  }

  @Bean(destroyMethod = "close")
  public AggregationsDbPort aggregationsDB(
      @Value("${db.aggregations.location}") String aggregationsLocation) {

    var aggregationsParentFolder = Paths.get(aggregationsLocation).getParent();

    if (!Files.exists(aggregationsParentFolder)) {
      try {
        Files.createDirectories(aggregationsParentFolder);
      } catch (IOException e) {
        throw new GatewayInitializationException(
            "Error creating parent folder for aggregations location '%s'".formatted(aggregationsLocation));
      }
    }

    var aggregationsDB = new AggregationsDbAdapter(aggregationsLocation);

    // Clean previous aggregations when bean is created
    aggregationsDB.getAllAgregations().entrySet().forEach(e -> {
      var interval = e.getKey();
      e.getValue().entrySet().forEach(e1 -> {
        var device = e1.getKey();
        e1.getValue().entrySet().forEach(e2 -> {
          var sensor = e2.getKey();
          aggregationsDB.releaseAggregation(device, sensor, interval);
        });
      });
    });

    return aggregationsDB;
  }

  @Bean(destroyMethod = "close")
  public SensorMetadataDbPort sensorMetadataDB(@Value("${db.metadata.location}") String metadataLocation) {
    var metadataParentFolder = Paths.get(metadataLocation).getParent();

    if (!Files.exists(metadataParentFolder)) {
      try {
        Files.createDirectories(metadataParentFolder);
      } catch (IOException e) {
        throw new GatewayInitializationException(
            "Error creating parent folder for metadata location '%s'".formatted(metadataLocation));
      }
    }

    return new SensorMetadataDbAdapter(metadataLocation);
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
  public CoapAdapter coapAdapter(DispatcherPort dispatcher) {
    return new CoapAdapter(dispatcher);
  }

  @Bean
  public SchedulerPort scheduler(AggregationsDbPort aggregationsDB, ExpressionsDbPort expressionsDB,
      MeasureProcessorPort measureProcessor) {
    // TODO: Output connectors
    var scheduler = new SchedulerService(aggregationsDB, expressionsDB, measureProcessor, Set.of());

    aggregationsDB.getAllIntervals().forEach(interval -> scheduler.schedule(interval));

    return scheduler;
  }

  @Bean
  public SensorConfigurationPort sensorConfiguration(AggregationsDbPort aggregationsDB,
      ExpressionsDbPort expressionsDB, SensorMetadataDbPort sensorMetadataDB, MeasureProcessorPort measureProcessor) {
    return new SensorConfigurationService(aggregationsDB, expressionsDB, sensorMetadataDB, measureProcessor);
  }
}
