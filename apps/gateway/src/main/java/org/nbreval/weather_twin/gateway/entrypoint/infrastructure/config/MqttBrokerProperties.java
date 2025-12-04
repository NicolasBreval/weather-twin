package org.nbreval.weather_twin.gateway.entrypoint.infrastructure.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.moquette.broker.config.IConfig;

/**
 * Configuration properties for the embedded MQTT broker used by
 * {@link MqttSensorListenerAdapter}.
 * 
 * Properties are prefixed with `entrypoint.listener.mqtt` and include:
 * <ul>
 * <li>port: the port on which the MQTT broker will listen for incoming
 * connections (default: 1883)</li>
 * <li>persistence.enabled: whether to enable message persistence (default:
 * true)</li>
 * <li>persistence.path: the file system path where the MQTT broker will store
 * its persistent data; if null or blank, persistence is disabled (default:
 * empty)</li>
 * </ul>
 */
@Component
@ConfigurationProperties(prefix = "entrypoint.listener.mqtt")
public class MqttBrokerProperties {

  /** The TCP port configured for MQTT broker */
  private int port = 1883;

  /** Persistence configuration for the MQTT broker */
  private Persistence persistence = new Persistence();

  /**
   * Gets the port on which the MQTT broker will listen for incoming connections.
   * 
   * @return the MQTT broker port
   */
  public int getPort() {
    return port;
  }

  /**
   * Sets the port on which the MQTT broker will listen for incoming connections.
   * 
   * @param port the MQTT broker port
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * Gets the persistence configuration for the MQTT broker.
   * 
   * @return the persistence configuration
   */
  public Persistence getPersistence() {
    return persistence;
  }

  /**
   * Sets the persistence configuration for the MQTT broker.
   * 
   * @param persistence the persistence configuration
   */
  public void setPersistence(Persistence persistence) {
    this.persistence = persistence;
  }

  /**
   * Converts the current properties to a {@link Properties} object suitable for
   * configuring the MQTT broker.
   * 
   * @return a Properties object with the MQTT broker configuration
   */
  public Properties toBrokerProperties() {
    var props = new Properties();
    props.put(IConfig.HOST_PROPERTY_NAME, "0.0.0.0");
    props.put(IConfig.PORT_PROPERTY_NAME, String.valueOf(port));
    props.put(IConfig.PERSISTENCE_ENABLED_PROPERTY_NAME, persistence.enabled);
    props.put(IConfig.DATA_PATH_PROPERTY_NAME, persistence.path);
    return props;
  }

  /**
   * Persistence configuration for the MQTT broker.
   */
  public static class Persistence {
    /** If is true, the MQTT broker listener is enabled */
    private boolean enabled = true;

    /** The file system path where the MQTT broker will store its persistent data */
    private String path = "";

    /**
     * Checks if persistence is enabled.
     * 
     * @return true if persistence is enabled, false otherwise
     */
    public boolean isEnabled() {
      return enabled;
    }

    /**
     * Sets whether persistence is enabled.
     * 
     * @param enabled true to enable persistence, false to disable
     */
    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    /**
     * Gets the file system path where the MQTT broker will store its persistent
     * data.
     * 
     * @return the persistence path
     */
    public String getPath() {
      return path;
    }

    /**
     * Sets the file system path where the MQTT broker will store its persistent
     * data.
     * 
     * @param path the persistence path
     */
    public void setPath(String path) {
      this.path = path;
    }
  }
}
