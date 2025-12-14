package org.nbreval.weather_twin.gateway.infrastructure.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.entity.JsonValue;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JacksonCustomModule extends SimpleModule {

  private final class JsonValueSerializer extends StdSerializer<JsonValue<?>> {
    protected JsonValueSerializer() {
      super(JsonValue.class, true);
    }

    @Override
    public void serialize(JsonValue<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      var insideValue = value.value();

      if (insideValue == null) {
        gen.writeNull();
      } else if (insideValue instanceof Integer i) {
        gen.writeNumber(i);
      } else if (insideValue instanceof Float f) {
        gen.writeNumber(f);
      } else if (insideValue instanceof String s) {
        gen.writeString(s);
      } else if (insideValue instanceof Boolean b) {
        gen.writeBoolean(b);
      } else if (insideValue instanceof Json j) {
        provider.findValueSerializer(Json.class, null).serialize(j, gen, provider);
      } else if (insideValue instanceof JsonArray a) {
        provider.findValueSerializer(JsonArray.class, null).serialize(a, gen, provider);
      } else {
        throw new IllegalArgumentException(
            "Invalid JsonValue internal type '%s'".formatted(insideValue.getClass().getSimpleName()));
      }
    }

  }

  private final class JsonValueDeserializer extends StdDeserializer<JsonValue<?>> {

    protected JsonValueDeserializer() {
      super(JsonValue.class);
    }

    @Override
    public JsonValue<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
      var node = p.getCodec().readTree(p);

      if (node instanceof ValueNode v) {
        if (v.isInt()) {
          return new JsonValue<>(v.intValue());
        } else if (v.isFloat() || v.isDouble()) {
          return new JsonValue<>(v.floatValue());
        } else if (v.isTextual()) {
          return new JsonValue<>(v.textValue());
        } else if (v.isBoolean()) {
          return new JsonValue<>(v.booleanValue());
        }
      } else if (node instanceof ArrayNode) {
        var deserialized = ctxt.findContextualValueDeserializer(ctxt.constructType(JsonArray.class), null)
            .deserialize(node.traverse(p.getCodec()), ctxt);
        if (deserialized instanceof JsonArray array) {
          return new JsonValue<>(array);
        }
      } else if (node instanceof ObjectNode) {
        var deserialized = ctxt.findContextualValueDeserializer(ctxt.constructType(Json.class), null)
            .deserialize(node.traverse(p.getCodec()), ctxt);
        if (deserialized instanceof Json json) {
          return new JsonValue<>(json);
        }
      }

      throw new IllegalArgumentException(
          "Invalid node, is not a valid instance of '%s'".formatted(JsonValue.class.getSimpleName()));
    }

  }

  private final class JsonArraySerializer extends StdSerializer<JsonArray> {

    protected JsonArraySerializer() {
      super(JsonArray.class, true);
    }

    @Override
    public void serialize(JsonArray value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeStartArray();

      for (var i = 0; i < value.size(); i++) {
        gen.writeObject(value.get(i));
      }

      gen.writeEndArray();
    }

  }

  private final class JsonArrayDeserializer extends StdDeserializer<JsonArray> {

    protected JsonArrayDeserializer() {
      super(JsonArray.class);
    }

    @Override
    public JsonArray deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
      var node = p.getCodec().readTree(p);

      if (node instanceof ArrayNode a) {
        var elements = new ArrayList<JsonValue<?>>();

        for (var i = 0; i < a.size(); i++) {
          JsonNode item = a.get(i);
          if (ctxt.findContextualValueDeserializer(ctxt.constructType(JsonValue.class), null)
              .deserialize(item.traverse(p.getCodec()), ctxt) instanceof JsonValue j) {
            elements.add(j);
          } else {
            throw new IllegalArgumentException("Invalid value for array item");
          }
        }

        return new JsonArray(elements.stream().toArray());
      }

      throw new IllegalArgumentException(
          "Invalid JSON node to be deserialized as '%s'".formatted(JsonArray.class.getSimpleName()));
    }

  }

  private final class JsonSerializer extends StdSerializer<Json> {

    protected JsonSerializer() {
      super(Json.class, true);
    }

    @Override
    public void serialize(Json value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeStartObject();

      for (var entry : value.entries()) {
        gen.writeFieldName(entry.getKey());
        gen.writeObject(entry.getValue());
      }

      gen.writeEndObject();
    }

  }

  private final class JsonDeserializer extends StdDeserializer<Json> {

    protected JsonDeserializer() {
      super(Json.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Json deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
      var node = p.getCodec().readTree(p);

      if (node instanceof ObjectNode o) {
        var elements = new ArrayList<Map.Entry<String, JsonValue<?>>>();

        var fieldNames = o.fieldNames();

        while (fieldNames.hasNext()) {
          String fieldName = fieldNames.next();

          JsonNode item = o.get(fieldName);
          if (ctxt.findContextualValueDeserializer(ctxt.constructType(JsonValue.class), null)
              .deserialize(item.traverse(p.getCodec()), ctxt) instanceof JsonValue j) {
            elements.add(Map.entry(fieldName, j));
          } else {
            throw new IllegalArgumentException("Invalid value for array item");
          }
        }

        return new Json(elements.toArray(new Map.Entry[0]));
      }

      throw new IllegalArgumentException(
          "Invalid JSON node to be deserialized as '%s'".formatted(Json.class.getSimpleName()));
    }

  }

  @Override
  public void setupModule(SetupContext context) {
    var serializers = new SimpleSerializers();
    serializers.addSerializer(new JsonValueSerializer());
    serializers.addSerializer(new JsonArraySerializer());
    serializers.addSerializer(new JsonSerializer());
    context.addSerializers(serializers);

    var deserializers = new SimpleDeserializers();
    deserializers.addDeserializer(JsonValue.class, new JsonValueDeserializer());
    deserializers.addDeserializer(JsonArray.class, new JsonArrayDeserializer());
    deserializers.addDeserializer(Json.class, new JsonDeserializer());
    context.addDeserializers(deserializers);
  }

}
