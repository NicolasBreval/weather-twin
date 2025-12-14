package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.entity.JsonValue;
import org.nbreval.weather_twin.gateway.infrastructure.serializer.JacksonCustomModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationTests {

  private static ObjectMapper mapper;

  @BeforeAll
  static void setup() {
    mapper = new ObjectMapper();
    mapper.registerModule(new JacksonCustomModule());
  }

  @Test
  void shouldSerializeAndDeserializeJsonValue() throws JsonProcessingException {
    var jsonValues = List.of(
        new JsonValue<>(1),
        new JsonValue<>(1.1f),
        new JsonValue<>("test"),
        new JsonValue<>(false));

    var expected = List.of(
        "1",
        "1.1",
        "\"test\"",
        "false");

    var serializedList = new ArrayList<String>();

    for (var i = 0; i < jsonValues.size(); i++) {
      var serialized = mapper.writeValueAsString(jsonValues.get(i));
      serializedList.add(serialized);
      assertEquals(expected.get(i), serialized);
    }

    for (var i = 0; i < jsonValues.size(); i++) {
      assertEquals(jsonValues.get(i), mapper.readValue(serializedList.get(i), JsonValue.class));
    }
  }

  @Test
  void shouldSerializeAndDeserializeJsonArray() throws JsonProcessingException {
    var jsonArray = new JsonArray(2, 2.2f, "test", true);

    var expected = "[2,2.2,\"test\",true]";

    var serialized = mapper.writeValueAsString(jsonArray);

    assertEquals(expected, serialized);

    assertEquals(jsonArray, mapper.readValue(serialized, JsonArray.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldSerializeAndDeserializeJson() throws JsonProcessingException {
    var json = new Json(
        Map.entry("intValue", 1),
        Map.entry("floatValue", 1.1f),
        Map.entry("stringValue", "test"),
        Map.entry("booleanValue", true),
        Map.entry("arrayValue", new JsonArray(2, 2.2f, "test2", false, new JsonArray(3, 3.3f, "test3", true))),
        Map.entry("jsonValue", new Json(Map.entry("a", 1), Map.entry("b", new JsonArray(1, 2, 3)))));

    var expected = "{\"intValue\":1,\"floatValue\":1.1,\"stringValue\":\"test\",\"booleanValue\":true,\"arrayValue\":[2,2.2,\"test2\",false,[3,3.3,\"test3\",true]],\"jsonValue\":{\"a\":1,\"b\":[1,2,3]}}";

    var serialized = mapper.writeValueAsString(json);

    assertEquals(expected, serialized);

    var deserialized = mapper.readValue(serialized, Json.class);

    assertEquals(json, deserialized);
  }

}
