package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTests {

  @Value("${coap.port}")
  private int coapPort;

  private String serverUri;

  @BeforeEach
  void setup() {
    serverUri = "coap://localhost:%d".formatted(coapPort);
  }

  @Test
  void shouldAllowCoapDiscovery() throws ConnectorException, IOException {
    var endpoint = "%s/.well-known/core".formatted(serverUri);
    var client = new CoapClient(endpoint);
    var response = client.get();

    assertEquals(ResponseCode.CONTENT, response.getCode());
    assertEquals("</weather-twin>", response.getResponseText());
  }

}
