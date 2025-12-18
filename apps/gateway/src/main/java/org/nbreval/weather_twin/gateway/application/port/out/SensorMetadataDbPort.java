package org.nbreval.weather_twin.gateway.application.port.out;

import org.nbreval.weather_twin.gateway.application.entity.SensorMetadata;
import org.nbreval.weather_twin.gateway.application.enumeration.SensorType;

public interface SensorMetadataDbPort {

  SensorMetadata addMetadata(String device, String sensor, SensorType type, String magnitude, String description);

  SensorMetadata removeMetadata(String device, String sensor);

  SensorMetadata getMetadata(String device, String sensor);

  void close();
}
