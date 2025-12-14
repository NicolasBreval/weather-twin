package org.nbreval.weather_twin.gateway.infrastructure.config;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.nbreval.weather_twin.gateway.infrastructure.adapter.CoapAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the CoAP server
 */
@Configuration
public class CoapConfig {

  /**
   * Creates the CoAP server instance to be deployed
   * 
   * @param port        Configured CoAP port
   * @param coapAdapter Instance of {@link CoapAdapter} to be registered as
   *                    resource
   * @return The configured CoAP server
   */
  @Bean(initMethod = "start", destroyMethod = "stop")
  public CoapServer coapServer(@Value("${coap.port}") int port, CoapAdapter coapAdapter) {
    var coapServer = new CoapServer();
    coapServer.addEndpoint(CoapEndpoint.builder().setPort(0).build());
    coapServer.add(coapAdapter);
    return coapServer;
  }

}
