package org.nbreval.weather_twin.gateway.infrastructure.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

/**
 * Map-like object used to interact with a MapDB instance.
 */
public class DBMap<K, V> implements Map<K, V> {

  /**
   * MapDB database instance.
   */
  private final DB db;

  /**
   * Map connected to MapDB database.
   */
  private final HTreeMap<K, V> internalMap;

  public DBMap(DB db, String name, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    this.db = db;
    this.internalMap = db.hashMap(name).keySerializer(keySerializer)
        .valueSerializer(valueSerializer).createOrOpen();
  }

  /**
   * Commits all in-memory changes and store it to disk.
   */
  public void commit() {
    db.commit();
  }

  @Override
  public int size() {
    return internalMap.size();
  }

  @Override
  public boolean isEmpty() {
    return internalMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return internalMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return internalMap.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return internalMap.get(key);
  }

  @Override
  public V put(K key, V value) {
    return internalMap.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return internalMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    internalMap.putAll(m);
  }

  @Override
  public void clear() {
    internalMap.clear();
  }

  @Override
  public Set<K> keySet() {
    return internalMap.keySet();
  }

  @Override
  public Collection<V> values() {
    return internalMap.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return internalMap.entrySet();
  }

}
