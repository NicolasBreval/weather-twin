package org.nbreval.weather_twin.gateway.application.enumeration;

import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;

public enum SensorType {
  TEMPERATURE(DataType.FLOAT),
  HUMIDITY(DataType.FLOAT),
  PROXIMITY(DataType.FLOAT),
  ACCELERATION(DataType.FLOAT),
  GIROSCOPE(DataType.JSON),
  GAS(DataType.FLOAT),
  PRESSURE(DataType.FLOAT),
  LEVEL(DataType.FLOAT),
  PHOTOELECTRIC(DataType.FLOAT),
  CUSTOM_INT(DataType.INTEGER),
  CUSTOM_FLOAT(DataType.FLOAT),
  CUSTOM_TEXT(DataType.TEXT),
  CUSTOM_BOOLEAN(DataType.BOOLEAN),
  CUSTOM_JSON(DataType.JSON);

  private DataType dataType;

  private SensorType(DataType dataType) {
    this.dataType = dataType;
  }

  public DataType dataType() {
    return dataType;
  }
}
