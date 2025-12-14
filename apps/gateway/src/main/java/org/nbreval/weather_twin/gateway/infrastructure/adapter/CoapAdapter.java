package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.util.Objects;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.nbreval.weather_twin.gateway.application.port.in.DispatcherPort;
import org.nbreval.weather_twin.gateway.infrastructure.enumeration.DataType;
import org.nbreval.weather_twin.gateway.infrastructure.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter used to receive messages from external IoT devices.
 * 
 * This adapter mounts a CoAP server, an application layer's protocol used to
 * communicate devices with low resources, using UDP.
 * Each message received via a CoAP request is sent to the dispatcher to be
 * processed.
 */
public class CoapAdapter extends CoapResource {

  /**
   * Logger object to show some information in the application's output.
   */
  private static final Logger logger = LoggerFactory.getLogger(CoapAdapter.class);

  /** Regex pattern used to validate URL requests. */
  private final String uriRegex;

  /** Dispatcher instance to send all received messages. */
  private final DispatcherPort dispatcherPort;

  public CoapAdapter(DispatcherPort dispatcherPort) {
    super("weather-twin");

    uriRegex = "measures\\/\\w+\\/\\w+";
    this.dispatcherPort = dispatcherPort;
  }

  /**
   * Handles all requests received by the server and process only the valid
   * measure messages.
   */
  @Override
  public void handlePOST(CoapExchange exchange) {
    try {
      // Identify device and sensor names
      var uri = exchange.getRequestOptions().getUriPathString();

      if (!uri.matches(uriRegex)) {
        throw new IllegalArgumentException(
            "The received request doesn't match tre required uri pattern 'measures/<device>/<sensor>'");
      }

      var uriParts = uri.split("/");
      var device = uriParts[1];
      var sensor = uriParts[2];
      DataType dataType = EnumUtil.safeValueOf(DataType.class, exchange.getQueryParameter("data_type"))
          .orElseThrow(() -> new IllegalArgumentException(
              "The received request hasn't the required query parameter 'data_type', or the query param hasn't a valid value"));

      var payload = exchange.getRequestPayload();

      logger.debug("New measure request received for '%s.%s'".formatted(device, sensor));

      // Check payload
      if (Objects.isNull(payload) || payload.length == 0)
        throw new IllegalArgumentException("The received payload must be not null");

      // Send message to dispatcher
      var value = dataType.getFormattedValue(payload);

      dispatcherPort.consume(device, sensor, value).subscribe(
          _ -> {
            // do nothing on processed item
          },
          error -> {
            logger.error("Error processing received measure", error.getCause());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
          },
          () -> exchange.respond(ResponseCode.CREATED));

    } catch (IllegalArgumentException e) {
      logger.error("Bad request", e);
      exchange.respond(ResponseCode.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      logger.error("Internal server error", e);
      exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
    }
  }

}
