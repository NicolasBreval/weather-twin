package org.nbreval.weather_twin.gateway.application.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.nbreval.weather_twin.gateway.application.entity.SensorRegistration;
import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;
import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.SensorMetadataDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.exception.AlreadyExistsException;
import org.nbreval.weather_twin.gateway.infrastructure.exception.NotExistsException;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Implements a {@link SensorConfigurationPort}.
 * 
 * This service is the responsible for interact with databases to configure
 * sensors on system, like their WTAL expressions, or their intervals.
 */
public class SensorConfigurationService implements SensorConfigurationPort {

  /**
   * Allows to interact with aggregations database.
   */
  private final AggregationsDbPort aggregationsDB;

  /**
   * Allows to interact with expressions database.
   */
  private final ExpressionsDbPort expressionsDB;

  /**
   * Allows to interact with sensor metadata database.
   */
  private final SensorMetadataDbPort sensorMetadataDB;

  public SensorConfigurationService(AggregationsDbPort aggregationsDB, ExpressionsDbPort expressionsDB,
      SensorMetadataDbPort sensorMetadabaDB) {
    this.aggregationsDB = aggregationsDB;
    this.expressionsDB = expressionsDB;
    this.sensorMetadataDB = sensorMetadabaDB;
  }

  @Override
  public Set<Long> registerSensor(String device, String sensor, String defaultValue,
      String aggregationExpression, String flushExpression, Set<Long> intervals, SensorType sensorType,
      String magnitude, String description) {

    // Check if sensor is already registered
    if (!aggregationsDB.getAggregations(device, sensor).isEmpty())
      throw new AlreadyExistsException(
          "Already exists a registration for '%s.%s'".formatted(device, sensor));

    // Register expressions
    expressionsDB.setAggregatorExpression(device, sensor, aggregationExpression);

    if (flushExpression != null)
      expressionsDB.setFlushExpression(device, sensor, flushExpression);

    // Register aggregated measure
    var currentIntervals = aggregationsDB.getAllIntervals();

    var defaultValueObj = sensorType.dataType().getFormattedValue(defaultValue.getBytes());
    intervals.forEach(interval -> aggregationsDB.registerAggregation(device,
        sensor, interval, sensorType.dataType(), defaultValueObj));

    aggregationsDB.applyChanges();

    // Register sensor metadata
    sensorMetadataDB.addMetadata(device, sensor, sensorType, magnitude, description);

    return intervals.stream().filter(i -> !currentIntervals.contains(i)).collect(Collectors.toSet());
  }

  @Override
  public Set<Long> unregisterSensor(String device, String sensor) {
    if (aggregationsDB.getAggregations(device, sensor).isEmpty())
      throw new NotExistsException("Doesn't exists any registration for '%s.%s'".formatted(device, sensor));

    // Release all aggregations
    var removed = aggregationsDB.getAggregations(device, sensor);
    removed.keySet().forEach(interval -> aggregationsDB.unregisterAggregation(device, sensor, interval));

    // Remove expressions
    expressionsDB.removeAggregatorExpression(device, sensor);
    expressionsDB.removeFlushExpression(device, sensor);

    aggregationsDB.applyChanges();

    // Remove sensor metadata
    sensorMetadataDB.removeMetadata(device, sensor);

    return removed.keySet().stream().filter(interval -> aggregationsDB.getAggregationsByInterval(interval).isEmpty())
        .collect(Collectors.toSet());
  }

  @Override
  public boolean unregisterSensor(String device, String sensor, long interval) {
    if (aggregationsDB.getAggregation(device, sensor, interval) == null)
      throw new NotExistsException("Doesn't exists any registration for '%s.%s'".formatted(device, sensor));

    // Unregister aggregations
    aggregationsDB.unregisterAggregation(device, sensor, interval);

    // If there are no more registrations for same device and sensor,
    var rest = aggregationsDB.getAggregations(device, sensor);
    if (rest.isEmpty()) {
      // Remove expressions
      expressionsDB.removeAggregatorExpression(device, sensor);
      expressionsDB.removeFlushExpression(device, sensor);
    }

    aggregationsDB.applyChanges();

    // Remove sensor metasata
    sensorMetadataDB.removeMetadata(device, sensor);

    return aggregationsDB.getAggregationsByInterval(interval).isEmpty();
  }

  @Override
  public void updateAggregationExpression(String device, String sensor, String expression) {
    expressionsDB.setAggregatorExpression(device, sensor, expression);
    aggregationsDB.getRegisteredIntervals(device, sensor)
        .forEach(interval -> aggregationsDB.releaseAggregation(device, sensor, interval));
  }

  @Override
  public void updateFlushExpression(String device, String sensor, String expression) {
    expressionsDB.setFlushExpression(device, sensor, expression);
  }

  @Override
  public Tuple2<List<Long>, List<Long>> updateIntervals(String device, String sensor, Set<Long> intervals) {
    var currentIntervals = aggregationsDB.getRegisteredIntervals(device, sensor);

    if (currentIntervals.isEmpty()) {
      throw new NotExistsException(
          "Doesn't exist a registration for '%s.%s".formatted(device, sensor));
    }

    var intervalsToAdd = intervals.stream().filter(i -> !currentIntervals.contains(i)).toList();
    var intervalsToDelete = currentIntervals.stream().filter(i -> !intervals.contains(i)).toList();

    var storedDefaultValue = aggregationsDB.getAggregation(device, sensor, intervalsToDelete.get(0));

    // Remove registration of intervals not present in update dto and currently
    // registered
    intervalsToDelete.forEach(interval -> aggregationsDB.unregisterAggregation(device, sensor, interval));

    // Add new intervals
    intervalsToAdd
        .forEach(interval -> aggregationsDB.registerAggregation(device, sensor, interval, storedDefaultValue.dataType(),
            storedDefaultValue.value()));

    aggregationsDB.applyChanges();

    var intervalsToUnschedule = intervalsToDelete.stream()
        .filter(i -> aggregationsDB.getAggregationsByInterval(i).isEmpty()).toList();
    var intervalsToSchedule = intervalsToAdd.stream().filter(i -> aggregationsDB.getAggregationsByInterval(i).isEmpty())
        .toList();

    return Tuples.of(intervalsToUnschedule, intervalsToSchedule);
  }

  @Override
  public Collection<SensorRegistration> getAllRegistrations() {
    var byDeviceAndSensor = new HashMap<Tuple2<String, String>, SensorRegistration>();

    aggregationsDB.getAllAgregations()
        .forEach((interval, byDevice) -> byDevice.forEach((device, bySensor) -> bySensor.forEach((sensor, agg) -> {
          byDeviceAndSensor
              .compute(Tuples.of(device, sensor), (k, v) -> {
                if (v == null) {
                  var aggExpression = expressionsDB.getAggregatorExpression(device, sensor);
                  var flushExpression = expressionsDB.getFlushExpression(device, sensor);
                  var metadata = sensorMetadataDB.getMetadata(device, sensor);

                  return new SensorRegistration(device, sensor, aggExpression, flushExpression,
                      agg.defaultValue().toString(), Set.of(interval), metadata.type(), metadata.magnitude(),
                      metadata.description());
                } else {
                  return new SensorRegistration(v.device(), v.sensor(), v.aggregationExpression(), v.flushExpression(),
                      v.defaultValue(), Stream.concat(v.intervals().stream(), Stream.of(interval))
                          .collect(Collectors.toUnmodifiableSet()),
                      v.sensorType(), v.magnitude(), v.description());
                }
              });
        })));

    return byDeviceAndSensor.values();
  }

}
