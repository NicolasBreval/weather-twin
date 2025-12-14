package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.util.Map;
import java.util.stream.Collectors;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.nbreval.weather_twin.gateway.application.port.out.ExpressionsDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.entity.DBMap;
import org.nbreval.weather_twin.gateway.infrastructure.entity.MeasureKey;
import org.nbreval.weather_twin.gateway.infrastructure.serializer.KryoSerializer;

/**
 * Implements a {@link ExpressionsDbPort}.
 * 
 * This adapter manages two MapDB-based databases used to store aggregation and
 * flush expressions for each registered device and sensor.
 * 
 * An aggregation expression represents the operation to perform when a new
 * measure is received by the system, and the flush expression represents the
 * operation to perform over the aggregated measures before to be sent to
 * external systems.
 */
public class ExpressionsDbAdapter implements ExpressionsDbPort {

  /**
   * Map-like object connected to MapDB database, used to store aggregation
   * expressions.
   */
  private final DBMap<MeasureKey, String> aggregatorsDB;

  /**
   * Map-like object connected to MapDB database, used to store flush expressions.
   */
  private final DBMap<MeasureKey, String> flushesDB;

  public ExpressionsDbAdapter(String aggregatorsLocation, String flushesLocation) {
    aggregatorsDB = new DBMap<>(createLocalDB(aggregatorsLocation), "aggregatorsDB",
        new KryoSerializer<>(MeasureKey.class), new KryoSerializer<>(String.class));
    flushesDB = new DBMap<>(createLocalDB(flushesLocation), "flushesDB", new KryoSerializer<>(MeasureKey.class),
        new KryoSerializer<>(String.class));
  }

  @Override
  public String getAggregatorExpression(String device, String sensor) {
    var key = new MeasureKey(device, sensor);
    return aggregatorsDB.get(key);
  }

  @Override
  public String setAggregatorExpression(String device, String sensor, String expression) {
    var key = new MeasureKey(device, sensor);
    var stored = aggregatorsDB.put(key, expression);
    aggregatorsDB.commit();
    return stored;
  }

  @Override
  public String getFlushExpression(String device, String sensor) {
    var key = new MeasureKey(device, sensor);
    return flushesDB.get(key);
  }

  @Override
  public String setFlushExpression(String device, String sensor, String expression) {
    var key = new MeasureKey(device, sensor);
    var stored = flushesDB.put(key, expression);
    flushesDB.commit();
    return stored;
  }

  @Override
  public Map<String, Map<String, String>> getAllAggregatorExpressions() {
    return aggregatorsDB.entrySet().stream()
        .collect(Collectors.groupingBy(e -> e.getKey().device(),
            Collectors.groupingBy(e -> e.getKey().sensor(),
                Collectors.reducing("", e -> e.getValue(), (a, b) -> b))));
  }

  @Override
  public String removeAggregatorExpression(String device, String sensor) {
    var key = new MeasureKey(device, sensor);
    var removed = aggregatorsDB.remove(key);
    aggregatorsDB.commit();
    return removed;
  }

  @Override
  public Map<String, Map<String, String>> getAllFlushExpressions() {
    return flushesDB.entrySet().stream()
        .collect(Collectors.groupingBy(e -> e.getKey().device(),
            Collectors.groupingBy(e -> e.getKey().sensor(),
                Collectors.reducing("", e -> e.getValue(), (a, b) -> b))));
  }

  @Override
  public String removeFlushExpression(String device, String sensor) {
    var key = new MeasureKey(device, sensor);
    var removed = flushesDB.remove(key);
    flushesDB.commit();
    return removed;
  }

  private DB createLocalDB(String location) {
    return DBMaker
        .fileDB(location)
        .transactionEnable()
        .closeOnJvmShutdown()
        .make();
  }
}
