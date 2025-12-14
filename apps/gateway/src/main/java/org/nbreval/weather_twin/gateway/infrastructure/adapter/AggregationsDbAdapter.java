package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapdb.DBMaker;
import org.nbreval.weather_twin.gateway.application.port.out.AggregationsDbPort;
import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.entity.JsonValue;
import org.nbreval.weather_twin.gateway.infrastructure.entity.DBMap;
import org.nbreval.weather_twin.gateway.infrastructure.entity.Aggregation;
import org.nbreval.weather_twin.gateway.infrastructure.entity.AggregationKey;
import org.nbreval.weather_twin.gateway.infrastructure.serializer.KryoSerializer;

/**
 * Implements an {@link AggregationsDBPort}.
 * 
 * This adapter manages the MapDB-based database used to store aggregated values
 * from measures, grouped by device, sensor and interval.
 * 
 * Each update operation in this adapter don't calls commit operation in
 * database, so it's required to invoke {@link #applyChanges()} method; this is
 * because this database will be used continuously by the system, so the
 * responsibility is externalized to make it more efficient.
 */
public class AggregationsDbAdapter implements AggregationsDbPort {

  /**
   * A map-like object wich represents the aggregations stored in MapDB.
   */
  private final DBMap<AggregationKey, Aggregation> db;

  public AggregationsDbAdapter() {
    db = new DBMap<>(DBMaker
        .tempFileDB()
        .fileChannelEnable()
        .transactionEnable()
        .closeOnJvmShutdown()
        .allocateStartSize(1024)
        .allocateIncrement(1024)
        .make(), "aggregations", new KryoSerializer<>(AggregationKey.class),
        new KryoSerializer<>(Aggregation.class, Float.class, Integer.class, Boolean.class, Json.class,
            JsonArray.class, JsonValue.class, String.class));
  }

  @Override
  public void registerAggregation(String device, String sensor, long interval, Object defaultValue) {
    var key = new AggregationKey(device, sensor, interval);
    db.computeIfAbsent(key, _ -> new Aggregation(defaultValue, defaultValue, 1));
  }

  @Override
  public void updateAggregation(String device, String sensor, long interval, Object aggregation) {
    var key = new AggregationKey(device, sensor, interval);
    db.computeIfPresent(key, (k, v) -> new Aggregation(aggregation, v.defaultValue(), v.steps() + 1));
  }

  @Override
  public Aggregation releaseAggregation(String device, String sensor, long interval) {
    var key = new AggregationKey(device, sensor, interval);
    return db.computeIfPresent(key, (k, v) -> new Aggregation(v.defaultValue(), v.defaultValue(), v.steps() + 1));
  }

  @Override
  public Aggregation removeAggregation(String device, String sensor, long interval) {
    var key = new AggregationKey(device, sensor, interval);
    return db.remove(key);
  }

  @Override
  public Map<Long, Aggregation> getAggregations(String device, String sensor) {
    return db.entrySet().stream().filter(e -> e.getKey().device().equals(device) && e.getKey().sensor().equals(sensor))
        .collect(Collectors.groupingBy(e -> e.getKey().interval(),
            Collectors.reducing(null, e -> e.getValue(), (a, b) -> b)));
  }

  @Override
  public void unregisterAggregation(String device, String sensor, long interval) {
    var key = new AggregationKey(device, sensor, interval);
    db.remove(key);
  }

  @Override
  public void applyChanges() {
    db.commit();
  }

  @Override
  public Map<Long, Map<String, Map<String, Aggregation>>> getAllAgregations() {
    return db.entrySet().stream()
        .collect(Collectors.groupingBy(e -> e.getKey().interval(),
            Collectors.groupingBy(e -> e.getKey().device(),
                Collectors.groupingBy(e -> e.getKey().sensor(),
                    Collectors.reducing(null, e -> e.getValue(), (a, b) -> a)))));
  }

  @Override
  public Map<String, Map<String, Aggregation>> getAggregationsByInterval(long interval) {
    return db.entrySet().stream().filter(e -> e.getKey().interval() == interval)
        .collect(Collectors.groupingBy(e -> e.getKey().device(),
            Collectors.groupingBy(e -> e.getKey().sensor(),
                Collectors.reducing(null, e -> e.getValue(), (a, b) -> b))));
  }

  @Override
  public Set<Long> getAllIntervals() {
    return db.keySet().stream().map(k -> k.interval()).collect(Collectors.toSet());
  }

  @Override
  public Set<Long> getRegisteredIntervals(String device, String sensor) {
    return db.keySet().stream().filter(k -> k.device().equals(device) && k.sensor().equals(sensor))
        .map(AggregationKey::interval).collect(Collectors.toSet());
  }

  @Override
  public Aggregation getAggregation(String device, String sensor, long interval) {
    return db.get(new AggregationKey(device, sensor, interval));
  }
}
