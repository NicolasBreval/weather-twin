package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import org.mapdb.DBMaker;
import org.nbreval.weather_twin.gateway.application.entity.SensorMetadata;
import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;
import org.nbreval.weather_twin.gateway.application.port.out.SensorMetadataDbPort;
import org.nbreval.weather_twin.gateway.infrastructure.entity.DBMap;
import org.nbreval.weather_twin.gateway.infrastructure.entity.SensorMetadataKey;
import org.nbreval.weather_twin.gateway.infrastructure.serializer.KryoSerializer;

public class SensorMetadataDbAdapter implements SensorMetadataDbPort {

  private final DBMap<SensorMetadataKey, SensorMetadata> metadataDB;

  public SensorMetadataDbAdapter(String location) {
    metadataDB = new DBMap<>(DBMaker.fileDB(location).transactionEnable().closeOnJvmShutdown().make(),
        "sensor-metadata", new KryoSerializer<>(SensorMetadataKey.class),
        new KryoSerializer<>(SensorMetadata.class, SensorType.class));
  }

  @Override
  public SensorMetadata addMetadata(String device, String sensor, SensorType type, String magnitude,
      String description) {
    var stored = metadataDB.put(new SensorMetadataKey(device, sensor),
        new SensorMetadata(device, sensor, type, magnitude, description));
    metadataDB.commit();
    return stored;
  }

  @Override
  public SensorMetadata removeMetadata(String device, String sensor) {
    var stored = metadataDB.remove(new SensorMetadataKey(device, sensor));
    metadataDB.commit();
    return stored;
  }

  @Override
  public SensorMetadata getMetadata(String device, String sensor) {
    return metadataDB.get(new SensorMetadataKey(device, sensor));
  }

  @Override
  public void close() {
    metadataDB.close();
  }

}
