package org.nbreval.weather_twin.gateway.infrastructure.enumeration;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents the type of data of a measure received from a sensor. It's used to
 * check the data received and transform it from a byte array to an object.
 */
public enum DataType {
  /**
   * Represents integer numeric types. Example:
   * 
   * <pre>{@code
   *    1, 5, 89
   * }</pre>
   */
  INTEGER,
  /**
   * Represents floating numeric types. Example:
   * 
   * <pre>{@code
   *  23, 4.5, 38492389.3434
   * }</pre>
   */
  FLOAT,
  /**
   * Represents boolean types. Example:
   * 
   * <pre>
   * {@code
   *  "true", "True", "false", "fAlse", 0x0, 0x1
   * }</code>
   */
  BOOLEAN,
  /**
   * Represents a text content.
   */
  TEXT,
  /**
   * Represents a valid JSON. Example:
   * 
   * <pre>{@code
   *  {
   *    "key": "value"
   *  }
   * }</pre>
   */
  JSON;

  /**
   * Makes easy to convert plain string to a JSON object
   */
  private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

  /**
   * Parses de byte array payload into a Java Object to be processed in the
   * system.
   * 
   * @param payload Byte array data.
   * @return The parsed object from input byte array data.
   */
  public Object getFormattedValue(byte[] payload) {
    switch (this) {
      case INTEGER:
        try {
          return Integer.parseInt(new String(payload));
        } catch (NumberFormatException e) {
          if (payload.length == 4) {
            var buffer = ByteBuffer.wrap(payload);
            return buffer.getInt();
          }
        }

        throw new IllegalArgumentException("Cannot parse INTEGER from payload");
      case FLOAT:
        try {
          return Float.parseFloat(new String(payload));
        } catch (NumberFormatException e) {
          if (payload.length == 4) {
            var buffer = ByteBuffer.wrap(payload);
            return buffer.getFloat();
          }
        }

        throw new IllegalArgumentException("Cannot parse FLOAT from payload");
      case BOOLEAN:
        String str = new String(payload).toLowerCase();
        if (str.equals("true") || str.equals("1"))
          return true;
        if (str.equals("false") || str.equals("0"))
          return false;

        throw new IllegalArgumentException("Cannot parse BOOLEAN from payload");
      case TEXT:
        return new String(payload);
      case JSON:
        try {
          var node = mapper.readTree(new String(payload));

          if (node instanceof ObjectNode oNode) {
            return parseJson(oNode);
          } else {
            throw new IllegalArgumentException("Cannot parse JSON from array payload");
          }

        } catch (IOException e) {
          // do nothing
        }

        throw new IllegalArgumentException("Cannot parse JSON from payload");
      default:
        throw new IllegalArgumentException("Unsupported data type");
    }
  }

  /**
   * Obtains a {@link Json} object from a {@link ObjectNode}.
   * 
   * @param node Jackson's object node which represents the {@link Json} content.
   * @return The {@link Json} related to node.
   */
  private Json parseJson(ObjectNode node) {
    var json = new Json();

    node.properties().forEach(entry -> {
      var fieldName = entry.getKey();
      var nodeValue = entry.getValue();

      if (nodeValue instanceof ObjectNode o) {
        var object = parseJson(o);
        json.add(fieldName, object);
      } else if (nodeValue instanceof ArrayNode a) {
        var array = parseJsonArray(a);
        json.add(fieldName, array);
      } else if (nodeValue.isIntegralNumber()) {
        json.add(fieldName, nodeValue.asInt());
      } else if (nodeValue.isFloatingPointNumber()) {
        json.add(fieldName, nodeValue.floatValue());
      } else if (nodeValue.isTextual()) {
        json.add(fieldName, nodeValue.textValue());
      } else if (nodeValue.isBoolean()) {
        json.add(fieldName, nodeValue.booleanValue());
      } else if (nodeValue.isNull()) {
        json.add(fieldName, null);
      } else {
        throw new IllegalArgumentException(
            "Invalid node value for field '%s' with value '%s'".formatted(fieldName, nodeValue.toString()));
      }
    });

    return json;
  }

  /**
   * Obtains a {@link JsonArray} from a {@link ArrayNode}.
   * 
   * @param node Jackson's array node which represents the {@link JsonArray}
   *             content.
   * @return The {@link JsonArray} related to node.
   */
  private JsonArray parseJsonArray(ArrayNode node) {
    var array = new JsonArray();

    for (var subNode : node) {
      if (subNode instanceof ObjectNode o) {
        var object = parseJson(o);
        array.add(object);
      } else if (subNode instanceof ArrayNode a) {
        var subArray = parseJsonArray(a);
        array.add(subArray);
      } else if (subNode.isIntegralNumber()) {
        array.add(subNode.asInt());
      } else if (subNode.isFloatingPointNumber()) {
        array.add(subNode.floatValue());
      } else if (subNode.isTextual()) {
        array.add(subNode.textValue());
      } else if (subNode.isBoolean()) {
        array.add(subNode.booleanValue());
      } else if (subNode.isNull()) {
        array.add(null);
      } else {
        throw new IllegalArgumentException(
            "Invalid node value for item with value '%s'".formatted(subNode.toString()));
      }
    }

    return array;
  }
}
