package org.nbreval.weather_twin.gateway.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.service.WtalLogicService;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalIllegalOperationException;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalParsingException;
import org.nbreval.weather_twin.gateway.infrastructure.util.AggregatorWtalChecker;
import org.nbreval.weather_twin.gateway.infrastructure.util.AggregatorWtalEvaluator;

import static java.util.Map.entry;

public class AggregatorWtalTests {

  private static class WtalExecutor {

    public Object execute(String expression, Map<String, Object> context) {
      return new AggregatorWtalEvaluator(new WtalLogicService(), context).evaluate(expression);
    }

    public Object execute(String expression) {
      return execute(expression, new HashMap<>());
    }

  }

  private final WtalExecutor executor = new WtalExecutor();

  // #region TYPING TESTS

  @SuppressWarnings("unchecked")
  @Test
  void shouldReturnTheCorrectValueType() {
    assertEquals(1, executor.execute("1;"));

    assertEquals(14.5f, executor.execute("14.5;"));

    assertEquals("Hi!!!!", executor.execute("\"Hi!!!!\";"));

    assertEquals(true, executor.execute("true;"));

    assertEquals(false, executor.execute("false;"));

    assertEquals(array(), executor.execute("[];"));

    assertEquals(array(1, 2.3f, "hello world", true, false, array()),
        executor.execute("[1, 2.3, \"hello world\", true, false, []];"));

    assertEquals(json(
        entry("int", 2),
        entry("float", 4.5f),
        entry("string", "Hi!"),
        entry("boolean", true),
        entry("array", array(
            1,
            5.6f,
            "ABCD",
            array(1, 2, 3),
            json(
                entry("key", "value")))),
        entry("object", json(
            entry("key", 3)))),
        executor.execute("""
            {
              "int": 2,
              "float": 4.5,
              "string": "Hi!",
              "boolean": true,
              "array": [
                1,
                5.6,
                "ABCD",
                [1,2,3],
                {"key": "value"}
              ],
              "object": {
                "key": 3
              }
            };
                """, Map.of()));
    assertEquals(-1, executor.execute("-1;"));
  }

  // #endregion

  // #region VARIABLES TESTS

  @Test
  void shouldParseVariablesCorrectly() {
    assertEquals(47, executor.execute("MY_VAR;", Map.of("MY_VAR", 47)));

    assertEquals(87.6f, executor.execute("MY_VAR;", Map.of("MY_VAR", 87.6f)));

    assertEquals("Hi!", executor.execute("MY_VAR;", Map.of("MY_VAR", "Hi!")));

    assertEquals(true, executor.execute("MY_VAR;", Map.of("MY_VAR", true)));

    assertEquals(false, executor.execute("MY_VAR;", Map.of("MY_VAR", false)));

    assertEquals(array(1, 2, 3), executor.execute("MY_VAR;", Map.of("MY_VAR", array(1, 2, 3))));

    assertEquals(47, executor.execute("MY_VAR = 47; MY_VAR;"));

    assertEquals(34.7f, executor.execute("MY_VAR = 34.7; MY_VAR;"));

    assertEquals("Hi!", executor.execute("MY_VAR = \"Hi!\"; MY_VAR;"));

    assertEquals(true, executor.execute("MY_VAR = true; MY_VAR;"));

    assertEquals(false, executor.execute("MY_VAR = false; MY_VAR;"));

    assertEquals(array(1, 2, 3), executor.execute("MY_VAR = [1, 2, 3]; MY_VAR;"));

  }

  // #endregion

  // #region ADD OPERATOR TESTS

  @Test
  void shouldSucceedOnAddOperation() {
    // Left number
    assertEquals(3, executor.execute("1 + 2;"));
    assertEquals(3.8f, executor.execute("1.5 + 2.3;"));

    // Left string
    assertEquals("OK2", executor.execute("\"OK\" + 2;"));
    assertEquals("OK!!!", executor.execute("\"OK\" + \"!!!\";"));
    assertEquals("OK true", executor.execute("\"OK \" + true;"));
    assertEquals("OK [1,2,3]", executor.execute("\"OK \" + [1,2,3];"));
    assertEquals("OK {\"key\":\"value\"}", executor.execute("\"OK \" + {\"key\": \"value\"};"));

    // Left list
    assertEquals(array(1, 2, 3), executor.execute("[1, 2] + 3;"));
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldFailOnAddOperation() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 + \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 + true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 + [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("""
        3 + {"key": 2};
        """));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false + 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false + \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false + true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false + [1, 2];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("""
        true + {};
        """));

    // Left list
    assertEquals(array(1, 2, "3"), executor.execute("[1, 2] + \"3\";"));
    assertEquals(array(1, 2, true), executor.execute("[1, 2] + true;"));
    assertEquals(array(1, 2, array(3, 4)), executor.execute("[1, 2] + [3, 4];"));
    assertEquals(array(1, 2, json(entry("key", "value"))), executor.execute("""
        [1, 2] + {"key": "value"};
            """));

    // Left object
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} + 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} + \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} + false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} + [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} + {};"));
  }

  // #endregion

  // #region SUBSTRACT OPERATION TESTS

  @Test
  void shouldSucceedOnSubstractOperation() {
    // Left number
    assertEquals(1, executor.execute("6 - 5;"));
    assertEquals(-2, executor.execute("6 - 8;"));
    assertEquals(4.0f, executor.execute("5.5 - 1.5;"));
    assertEquals(-1.0f, executor.execute("5.5 - 6.5;"));
  }

  @Test
  void shouldFailOnSubstractOperation() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("6 - \"5\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("6 - true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("6 - [1, 2];"));

    // Left string
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"Hi!\" - 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"Hi!\" - \"Hi!\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"Hi!\" - true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"Hi!\" - [];"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false - 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false - \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false - true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false - [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false - {};"));

    // Left list
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] - 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] - \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] - true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] - [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] - {};"));

    // Left object
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} - 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} - \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} - true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} - [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} - {};"));
  }

  // #endregion

  // #region MULTIPLY OPERATION TESTS

  @Test
  void shouldSucceedOnMultiplyOperation() {
    // Left number
    assertEquals(15, executor.execute("3 * 5;"));
    assertEquals(3 * 56.8f, executor.execute("3 * 56.8;"));

    // Left string
    assertEquals("HOLAHOLAHOLA", executor.execute("\"HOLA\" * 3;"));
  }

  @Test
  void shouldFailOnMultiplyOperation() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 * \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 * true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 * [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 * {};"));

    // Left string
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" * \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" * true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" * [1, 2];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" * {};"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true * 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true * \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true * false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true * [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true * {};"));

    // Left list
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[1, 2] * 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] * \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] * false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] * [1, 2, 3];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] * {};"));
  }

  // #endregion

  // #region DIVISION OPERATION TESTS

  @Test
  void shouldSucceedOnDivisionOperation() {
    // Left number
    assertEquals(0, executor.execute("3 / 8;"));
    assertEquals(3f / 8, executor.execute("3.0 / 8;"));
    assertEquals(3f / 8, executor.execute("3 / 8.0;"));
  }

  @Test
  void shouldFailOnDivisionOperation() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / 0;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / 0.0;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3.0 / 0;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3.0 / 0.0;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("3 / {};"));

    // Left string
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" / 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" / \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" / true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" / [1, 2];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" / {};"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false / 6;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false / \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false / true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false / [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false / {};"));

    // Left list
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] / 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] / \"C\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] / true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] / [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] / {};"));

    // Left object
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} / 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} / \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} / true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} / [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} / {};"));
  }

  // #endregion

  // #region AND OPERATOR TESTS

  @SuppressWarnings("unchecked")
  @Test
  void shouldSucceedOnAndOperation() {
    assertEquals(true, executor.execute("true & true;"));
    assertEquals(false, executor.execute("true & false;"));
    assertEquals(false, executor.execute("false & false;"));

    assertEquals(array(1, 2, 3, 4), executor.execute("[1, 2, 3] & [4];"));
    assertEquals(json(entry("a", 1), entry("b", 3), entry("c", 4)), executor.execute("""
        {
          "a": 1,
          "b": 2
        } & {
          "b": 3,
          "c": 4
        };
        """));
  }

  @Test
  void shouldFailOnAndOperation() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 & 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 & \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 & true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 & [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 & {};"));

    // Left String
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"a\" & 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"a\" & \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"a\" & true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"a\" & [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"a\" & {};"));

    // Left Boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false & 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false & \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false & [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false & {};"));

    // Left List
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] & 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] & \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] & true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] & {};"));

    // Left Object
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} & 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} & \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} & true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} & [];"));
  }

  // #endregion

  // #region ARRAY ACCESOR OPERATOR TESTS

  @Test
  void shouldSucceedOnArrayAccesorOperation() {
    assertEquals(1, executor.execute("[1,2,3][0];"));
    assertEquals(2, executor.execute("ARR = [1,2,3]; ARR[1];"));
    assertEquals(2, executor.execute("ARR = [1,2,3]; ARR[0 + 1];"));
    assertEquals(3, executor.execute("ARR = [1,2,3]; ARR[-1];"));
    assertEquals(array(2), executor.execute("ARR = [1,2,3]; ARR[1:-1];"));
    assertEquals("C", executor.execute("\"ABCD\"[2];"));
    assertEquals("CD", executor.execute("\"ABCD\"[2:4];"));
  }

  @Test
  void shouldFailOnArrayAccessorOperation() {
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1[2];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("false[2];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{}[2];"));
  }

  // #endregion

  // #region COMPARING OPERATOR TESTS

  @Test
  void shouldSuccessOnComparing() {
    assertEquals(true, executor.execute("3 > 1;"));
    assertEquals(false, executor.execute("3 > 10;"));
    assertEquals(true, executor.execute("3 < 10;"));
    assertEquals(false, executor.execute("3 < 1;"));
    assertEquals(true, executor.execute("3 == 3;"));
    assertEquals(false, executor.execute("3 == 4;"));
    assertEquals(true, executor.execute("3 != 4;"));
    assertEquals(false, executor.execute("3 != 3;"));
    assertEquals(false, executor.execute("3 == \"A\";"));
    assertEquals(true, executor.execute("3 != \"A\";"));
    assertEquals(false, executor.execute("3 == true;"));
    assertEquals(true, executor.execute("3 != true;"));
    assertEquals(false, executor.execute("3 == [];"));
    assertEquals(true, executor.execute("3 != [];"));
    assertEquals(false, executor.execute("3 == {};"));
    assertEquals(true, executor.execute("3 != {};"));
    assertEquals(true, executor.execute("3 >= 1;"));
    assertEquals(true, executor.execute("3 >= 3;"));
    assertEquals(false, executor.execute("3 >= 5;"));
    assertEquals(true, executor.execute("3 <= 4;"));
    assertEquals(true, executor.execute("3 <= 3;"));
    assertEquals(false, executor.execute("3 <= 1;"));

    assertEquals(false, executor.execute("\"A\" == 3;"));
    assertEquals(true, executor.execute("\"A\" != 3;"));
    assertEquals(true, executor.execute("\"A\" == \"A\";"));
    assertEquals(false, executor.execute("\"A\" == \"B\";"));
    assertEquals(false, executor.execute("\"A\" != \"A\";"));
    assertEquals(true, executor.execute("\"A\" != \"B\";"));
    assertEquals(false, executor.execute("\"A\" == true;"));
    assertEquals(true, executor.execute("\"A\" != false;"));
    assertEquals(false, executor.execute("\"A\" == [];"));
    assertEquals(true, executor.execute("\"A\" != [];"));
    assertEquals(false, executor.execute("\"A\" == {};"));
    assertEquals(true, executor.execute("\"A\" != {};"));

    assertEquals(false, executor.execute("true == 3;"));
    assertEquals(true, executor.execute("true != 3;"));
    assertEquals(false, executor.execute("true == \"A\";"));
    assertEquals(true, executor.execute("true != \"B\";"));
    assertEquals(true, executor.execute("true == true;"));
    assertEquals(false, executor.execute("true == false;"));
    assertEquals(false, executor.execute("true != true;"));
    assertEquals(true, executor.execute("true != false;"));
    assertEquals(false, executor.execute("true == [];"));
    assertEquals(true, executor.execute("true != [];"));
    assertEquals(false, executor.execute("true == {};"));
    assertEquals(true, executor.execute("true != {};"));

    assertEquals(false, executor.execute("[1,2,3] == 1;"));
    assertEquals(true, executor.execute("[1,2,3] != 1;"));
    assertEquals(false, executor.execute("[1,2,3] == \"A\";"));
    assertEquals(true, executor.execute("[1,2,3] != \"B\";"));
    assertEquals(false, executor.execute("[1,2,3] == true;"));
    assertEquals(true, executor.execute("[1,2,3] != false;"));
    assertEquals(true, executor.execute("[1,2,3] == [1,2,3];"));
    assertEquals(false, executor.execute("[1,2,3] == [1,2,3,4];"));
    assertEquals(false, executor.execute("[1,2,3] != [1,2,3];"));
    assertEquals(true, executor.execute("[1,2,3] != [1,2,3,4];"));
    assertEquals(false, executor.execute("[1,2,3] == {};"));
    assertEquals(true, executor.execute("[1,2,3] != {};"));

    assertEquals(false, executor.execute("{\"key\":1} == 1;"));
    assertEquals(true, executor.execute("{\"key\":1} != 1;"));
    assertEquals(false, executor.execute("{\"key\":1} == \"A\";"));
    assertEquals(true, executor.execute("{\"key\":1} != \"A\";"));
    assertEquals(false, executor.execute("{\"key\":1} == true;"));
    assertEquals(true, executor.execute("{\"key\":1} != true;"));
    assertEquals(false, executor.execute("{\"key\":1} == [];"));
    assertEquals(true, executor.execute("{\"key\":1} != [];"));
    assertEquals(true, executor.execute("{\"key\":1} == {\"key\":1};"));
    assertEquals(false, executor.execute("{\"key\":1} == {\"key\":2};"));
    assertEquals(false, executor.execute("{\"key\":1} != {\"key\":1};"));
    assertEquals(true, executor.execute("{\"key\":1} != {\"key\":2};"));
  }

  @Test
  void shouldFailOnComparing() {
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 > \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 < \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 >= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 <= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 > true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 < false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 >= true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 <= false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 > [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 < [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 >= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 <= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 > {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 < {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 >= {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("2 <= {};"));

    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" > 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" < 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" >= 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" <= 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" > \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" < \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" >= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" <= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" > true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" < false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" >= true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" <= false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" > [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" < [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" >= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" <= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" > {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" < {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" >= {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" <= {};"));

    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] > 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] < 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] >= 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] <= 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] > \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] < \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] >= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] <= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] > true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] < false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] >= true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] <= false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] > [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] < [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] >= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] <= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] > {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] < {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] >= {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] <= {};"));

    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} > 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} < 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} >= 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} <= 3;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} > \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} < \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} >= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} <= \"B\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} > true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} < false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} >= true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} <= false;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} > [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} < [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} >= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} <= [];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} > {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} < {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} >= {};"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} <= {};"));
  }

  // #endregion

  // #region OBJECT ACCESSOR OPERATOR TESTS

  @SuppressWarnings("unchecked")
  @Test
  void shouldSucceedOnObjectAccessorOperator() {
    assertEquals(1, executor.execute("{\"key\":1}[\"key\"];"));
    assertEquals("A", executor.execute("{\"key\":\"A\"}[\"key\"];"));
    assertEquals(true, executor.execute("{\"key\":true}[\"key\"];"));
    assertEquals(array(), executor.execute("{\"key\":[]}[\"key\"];"));
    assertEquals(json(), executor.execute("{\"key\":{}}[\"key\"];"));
    assertEquals(2, executor.execute("""
        VAR = {"key": { "key1": 2 }};
        VAR["key"]["key1"];
        """));
  }

  @Test
  void shouldFailOnObjectAccessorOperator() {
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1[\"key\"];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\"[\"key\"];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true[\"key\"];"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[][\"key\"];"));
  }

  // #endregion

  // #region IN OPERATOR TESTS

  @Test
  void shouldSuccessOnInOperator() {
    assertEquals(true, executor.execute("\"i\" IN \"Hi!!!\";"));
    assertEquals(true, executor.execute("\"\\d\\w\" IN \"12345678X\";"));
    assertEquals(false, executor.execute("\"o\" IN \"Hi!!!\";"));
    assertEquals(false, executor.execute("\"\\d\\w[2]\" IN \"12345678X\";"));

    assertEquals(true, executor.execute("4 IN [1,2,3,4,5];"));
    assertEquals(true, executor.execute("\"F\" IN [1,\"B\",3,\"D\",5, \"F\"];"));
    assertEquals(false, executor.execute("6 IN [1,2,3,4,5];"));
    assertEquals(false, executor.execute("\"X\" IN [1,\"B\",3,\"D\",5, \"F\"];"));

    assertEquals(true, executor.execute("\"key\" IN {\"key\": 1};"));
    assertEquals(false, executor.execute("\"key2\" IN {\"key\": 1};"));
  }

  @Test
  void shouldFailOnInOperator() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 IN 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 IN \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 IN true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("1 IN {};"));

    // Left string
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" IN 2;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("\"A\" IN true;"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true IN 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true IN \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("true IN {};"));

    // Left array
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] IN 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] IN \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] IN true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("[] IN {};"));

    // Left object
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} IN 1;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} IN \"A\";"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} IN true;"));
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("{} IN {};"));
  }

  // #endregion

  // #region FILTER FUNCTION TESTS

  @Test
  void shouldSucceedOnFilter() {
    assertEquals(array(5, 6), executor.execute("FILTER([1, 2, 3, 4, 5, 6], item -> item > 4);"));
    assertEquals(array(1, 2, 3), executor.execute("FILTER([1, 2, 3, 4, 5, 6], item -> item < 4);"));
    assertEquals(array(1, 2, 3, 4),
        executor.execute("FILTER([1, 2, 3, 4, 5, 6], item -> item <= 4);"));
    assertEquals(array("Chandler", "Rachel"), executor.execute(
        "FILTER([\"Joey\",\"Chandler\", \"Monica\", \"Rachel\", \"Phoebe\", \"Ross\"], item -> \"(?i)ch\" IN item);"));
    assertEquals(array(2, 4, 6),
        executor.execute("FILTER([1, 2, 3, 4, 5, 6], item -> item % 2 == 0);"));
    assertEquals(array(1, 3, 5),
        executor.execute("FILTER([1, 2, 3, 4, 5, 6], item -> item % 2 != 0);"));
  }

  @Test
  void shouldFailOnFilter() {
    // Left number
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER(1, item -> item > 0);"));

    // Left string
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER(\"A\", item -> item > 0);"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER(true, item -> item > 0);"));

    // Left object
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER({\"k\":1}, item -> item > 0);"));

    // Lambda returns non-boolean -> number
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER([1,2,3], item -> item + 1);"));

    // Lambda returns non-boolean -> string
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER([1,2,3], item -> \"A\");"));

    // Lambda returns non-boolean -> array
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER([1,2,3], item -> [item]);"));

    // Lambda returns non-boolean -> object
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("FILTER([1,2,3], item -> {\"k\": item});"));
  }

  // #endregion

  // #region MAP FUNCTION TESTS

  @Test
  void shouldSucceedOnMap() {
    // map numbers -> increment
    assertEquals(array(2, 3, 4), executor.execute("MAP([1,2,3], item -> item + 1);"));

    // map strings -> append
    assertEquals(array("aX", "bX", "cX"),
        executor.execute("MAP([\"a\",\"b\",\"c\"], item -> item + \"X\");"));

    // map to different types (array nodes preserved)
    assertEquals(array(array(1), array(2), array(3)),
        executor.execute("MAP([1,2,3], item -> [item]);"));
  }

  @Test
  void shouldFailOnMap() {
    // Left number
    assertThrows(WtalIllegalOperationException.class, () -> executor.execute("MAP(1, item -> item);"));

    // Left string
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("MAP(\"A\", item -> item);"));

    // Left boolean
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("MAP(true, item -> item);"));

    // Left object
    assertThrows(WtalIllegalOperationException.class,
        () -> executor.execute("MAP({\"k\":1}, item -> item);"));

    // Right not a lambda -> number
    assertThrows(WtalParsingException.class, () -> executor.execute("MAP([1,2,3], 1);"));

    // Right not a lambda -> string
    assertThrows(WtalParsingException.class, () -> executor.execute("MAP([1,2,3], \"A\");"));

    // Right not a lambda -> boolean
    assertThrows(WtalParsingException.class, () -> executor.execute("MAP([1,2,3], true);"));

    // Right not a lambda -> array
    assertThrows(WtalParsingException.class, () -> executor.execute("MAP([1,2,3], [1]);"));

    // Right not a lambda -> object
    assertThrows(WtalParsingException.class, () -> executor.execute("MAP([1,2,3], {\"k\":1});"));
  }

  // #endregion

  // #region CHECKER FUNCTION TESTS

  @Test
  void shouldFailOnInvalidExpression() {
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("1 + ;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("IDENTIFIER = * 5;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("SQRT();"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("POW(10, );"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("10 IN;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("a <= < b;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("MAP(list, x -> x * 2, 5);"));
    assertThrows(WtalParsingException.class,
        () -> AggregatorWtalChecker.checkExpression("IDENTIFIER = (10 + 5) - / 2;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("a & | b;"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("{\"key\": 10, };"));
    assertThrows(WtalParsingException.class,
        () -> AggregatorWtalChecker.checkExpression("[1, 2, \"texto\" \"otro\"];"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("{\"clave\"  10};"));
    assertThrows(WtalParsingException.class, () -> AggregatorWtalChecker.checkExpression("10 + 5 IDENTIFIER = 20;"));
  }

  // #endregion

  // #region ISNULL FUNCTION TESTS

  @Test
  void shouldSuccessOnIsNull() {
    var nullContext = new HashMap<String, Object>();
    nullContext.put("current", null);
    assertEquals(true, executor.execute("ISNULL(current);", nullContext));
    assertEquals(false, executor.execute("ISNULL(current);", Map.of("current", 2)));
    assertEquals(false, executor.execute("ISNULL(1 + 2);"));
    assertEquals(true, executor.execute("ISNULL([1,2,3][3]);"));
    assertEquals(false, executor.execute("ISNULL(\"ABCDEFG\"[-1]);"));
    assertEquals(true, executor.execute("ISNULL(\"ABCDEFG\"[20]);"));

  }

  // #endregion

  // #region TEST UTIL FUNCTIONS

  JsonArray array(Object... values) {
    return new JsonArray(values);
  }

  @SuppressWarnings("unchecked")
  Json json(Map.Entry<String, Object>... entries) {
    return new Json(entries);
  }

  // #endregion
}
