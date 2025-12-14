package org.nbreval.weather_twin.gateway.application.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;
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

  public SensorConfigurationService(AggregationsDbPort aggregationsDB, ExpressionsDbPort expressionsDB) {
    this.aggregationsDB = aggregationsDB;
    this.expressionsDB = expressionsDB;
  }

  @Override
  public Set<Long> registerSensor(String device, String sensor, DataType dataType, String defaultValue,
      String aggregationExpression, String flushExpression, Set<Long> intervals) {

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

    var defaultValueObj = dataType.getFormattedValue(defaultValue.getBytes());
    intervals.forEach(interval -> aggregationsDB.registerAggregation(device,
        sensor, interval, defaultValueObj));

    aggregationsDB.applyChanges();

    return intervals.stream().filter(i -> !currentIntervals.contains(i)).collect(Collectors.toSet());
  }

  @Override
  public Set<Long> unregisterSensor(String device, String sensor) {
    if (aggregationsDB.getAggregations(device, sensor).isEmpty())
      throw new NotExistsException("Doesn't exists any registration for '%s.%s'".formatted(device, sensor));

    // Release all aggregations
    var removed = aggregationsDB.getAggregations(device, sensor);
    removed.keySet().forEach(interval -> aggregationsDB.removeAggregation(device, sensor, interval));

    // Remove expressions
    expressionsDB.removeAggregatorExpression(device, sensor);
    expressionsDB.removeFlushExpression(device, sensor);

    aggregationsDB.applyChanges();

    return removed.keySet().stream().filter(interval -> aggregationsDB.getAggregationsByInterval(interval).isEmpty())
        .collect(Collectors.toSet());
  }

  @Override
  public boolean unregisterSensor(String device, String sensor, long interval) {
    if (aggregationsDB.getAggregation(device, sensor, interval) == null)
      throw new NotExistsException("Doesn't exists any registration for '%s.%s'".formatted(device, sensor));

    // Unregister aggregations
    aggregationsDB.removeAggregation(device, sensor, interval);

    // If there are no more registrations for same device and sensor,
    var rest = aggregationsDB.getAggregations(device, sensor);
    if (rest.isEmpty()) {
      // Remove expressions
      expressionsDB.removeAggregatorExpression(device, sensor);
      expressionsDB.removeFlushExpression(device, sensor);
    }

    aggregationsDB.applyChanges();

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
        .forEach(interval -> aggregationsDB.registerAggregation(device, sensor, interval, storedDefaultValue.value()));

    aggregationsDB.applyChanges();

    var intervalsToUnschedule = intervalsToDelete.stream()
        .filter(i -> aggregationsDB.getAggregationsByInterval(i).isEmpty()).toList();
    var intervalsToSchedule = intervalsToAdd.stream().filter(i -> aggregationsDB.getAggregationsByInterval(i).isEmpty())
        .toList();

    return Tuples.of(intervalsToUnschedule, intervalsToSchedule);
  }

}
