package org.nbreval.weather_twin.gateway.infrastructure.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.nbreval.weather_twin.gateway.application.port.in.WTALLogicPort;
import org.nbreval.weather_twin.gateway.domain.entity.Json;
import org.nbreval.weather_twin.gateway.domain.entity.JsonArray;
import org.nbreval.weather_twin.gateway.domain.entity.JsonValue;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalIllegalOperationException;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalParsingException;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalUndefinedVariableException;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalBaseVisitor;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalLexer;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.AccesorContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.AndOrContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.AssignStatementContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.AtomContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.ComparatorsContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.EvalStatementContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.ExpressionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.FilterFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.InContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.IsNullFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.Json_arrayContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.Json_objectContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.Json_pairContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.Json_valueContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.Lambda_expressionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.MapFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.ModContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.MulDivContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.ParenthesisContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.PlusMinusContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.PowFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.SqrtFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.StartContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.TernaryFunctionContext;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser.UnaryMinusContext;

public class AggregatorWtalEvaluator extends AggregatorWtalBaseVisitor<Object> {
  private final Map<String, Object> variableContext;

  private final WTALLogicPort wtalLogicPort;

  public AggregatorWtalEvaluator(WTALLogicPort wtalLogicPort, Map<String, Object> variableContext) {
    this.variableContext = variableContext;
    this.wtalLogicPort = wtalLogicPort;
  }

  public Object evaluate(String expression) {
    var stream = CharStreams.fromString(expression);
    var lexer = new AggregatorWtalLexer(stream);
    var tokens = new CommonTokenStream(lexer);

    var parser = new AggregatorWtalParser(tokens);

    var errorListener = new AggregatorWtalErrorListener();
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);

    var tree = parser.start();

    if (errorListener.hasErrors()) {
      throw new WtalParsingException(errorListener.getErrors());
    }

    return this.visit(tree);
  }

  @Override
  public Object visitStart(StartContext ctx) {
    Object finalResult = null;

    for (var statement : ctx.statement()) {
      finalResult = visit(statement);
    }

    return finalResult;
  }

  @Override
  public Object visitParenthesis(ParenthesisContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public Object visitAssignStatement(AssignStatementContext ctx) {
    if (ctx.IDENTIFIER() == null || ctx.expression() == null) {
      throw new WtalParsingException("Invalid assignment statement");
    }

    var varName = ctx.IDENTIFIER().getText();
    var value = visit(ctx.expression());

    variableContext.put(varName, value);

    return null;
  }

  @Override
  public Object visitAtom(AtomContext ctx) {
    if (ctx.NUMBER() != null) {
      var text = ctx.NUMBER().getText();

      if (text.contains(".")) {
        return Float.parseFloat(text);
      } else {
        return Integer.parseInt(text);
      }
    }

    if (ctx.STRING() != null) {
      var text = ctx.STRING().getText();
      return text.substring(1, text.length() - 1);
    }

    if (ctx.BOOLEAN() != null) {
      return ctx.BOOLEAN().getText().equals("true");
    }

    if (ctx.json_object() != null) {
      return visit(ctx.json_object());
    }

    if (ctx.json_array() != null) {
      return visit(ctx.json_array());
    }

    if (ctx.NULL() != null) {
      return null;
    }

    if (ctx.IDENTIFIER() != null) {
      var varName = ctx.IDENTIFIER().getText();
      if (variableContext.containsKey(varName)) {
        return variableContext.get(varName);
      } else {
        throw new WtalUndefinedVariableException("Undefined variable '%s'".formatted(varName));
      }
    }

    return null;
  }

  @Override
  public Object visitEvalStatement(EvalStatementContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public JsonValue<?> visitJson_value(Json_valueContext ctx) {
    return new JsonValue<>(visit(ctx.atom()));
  }

  @Override
  public Map.Entry<String, JsonValue<?>> visitJson_pair(Json_pairContext ctx) {
    var key = ctx.STRING().getText();
    var value = visitJson_value(ctx.json_value());

    return Map.entry(key.substring(1, key.length() - 1), value);
  }

  @Override
  public Json visitJson_object(Json_objectContext ctx) {
    var entries = new HashMap<String, Object>();

    for (var pair : ctx.json_pair()) {
      var entry = visitJson_pair(pair);
      entries.put(entry.getKey(), entry.getValue());
    }

    return new Json(entries);
  }

  @Override
  public Object visitJson_array(Json_arrayContext ctx) {
    var list = new ArrayList<JsonValue<?>>();

    for (var val : ctx.json_value()) {
      var value = visitJson_value(val);
      list.add(value);
    }

    return new JsonArray(list);
  }

  @Override
  public Object visitPlusMinus(PlusMinusContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));
    var op = ctx.op.getText();

    try {
      if (op.equals("+")) {
        return wtalLogicPort.applyAdd(left, right);
      } else if (op.equals("-")) {
        return wtalLogicPort.applySubstract(left, right);
      }
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }

    throw new WtalParsingException("Unknown operator '%s'".formatted(op));
  }

  @Override
  public Object visitMulDiv(MulDivContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));
    var op = ctx.op.getText();

    try {
      if (op.equals("*")) {
        return wtalLogicPort.applyProduct(left, right);
      } else if (op.equals("/")) {
        return wtalLogicPort.applyDivision(left, right);
      }
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }

    throw new WtalParsingException("Unknown operator '%s'".formatted(op));
  }

  @Override
  public Object visitAndOr(AndOrContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));
    String op = ctx.op.getText();

    try {
      if (op.equals("&")) {
        return wtalLogicPort.applyAnd(left, right);
      } else if (op.equals("|")) {
        return wtalLogicPort.applyOr(left, right);
      }
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }

    throw new WtalParsingException("Unknown operator '%s'".formatted(op));
  }

  @Override
  public Object visitComparators(ComparatorsContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));
    var op = ctx.op.getText();

    try {
      if (op.equals(">")) {
        return wtalLogicPort.applyGreaterThan(left, right);
      } else if (op.equals("<")) {
        return wtalLogicPort.applyLessThan(left, right);
      } else if (op.equals(">=")) {
        return wtalLogicPort.applyGreaterThanOrEquals(left, right);
      } else if (op.equals("<=")) {
        return wtalLogicPort.applyLessThanOrEquals(left, right);
      } else if (op.equals("==")) {
        return wtalLogicPort.applyEquals(left, right);
      } else if (op.equals("!=")) {
        return wtalLogicPort.applyNotEquals(left, right);
      }
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }

    throw new WtalParsingException("Unknown operator '%s'".formatted(op));
  }

  @Override
  public Object visitUnaryMinus(UnaryMinusContext ctx) {
    try {
      return wtalLogicPort.applyNegative(visit(ctx.expression()));
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitMod(ModContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));

    try {
      return wtalLogicPort.applyModulo(left, right);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitIn(InContext ctx) {
    var left = visit(ctx.expression(0));
    var right = visit(ctx.expression(1));

    try {
      return wtalLogicPort.applyIn(right, left);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitPowFunction(PowFunctionContext ctx) {
    var oBase = visit(ctx.expression(0));
    var oExponent = visit(ctx.expression(1));

    try {
      return wtalLogicPort.applyPow(oBase, oExponent);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitSqrtFunction(SqrtFunctionContext ctx) {
    var oValue = visit(ctx.expression());

    try {
      return wtalLogicPort.applySqrt(oValue);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitAccesor(AccesorContext ctx) {
    var left = visit(ctx.expression(0));
    var right1 = visit(ctx.expression(1));
    var right2 = ctx.expression().size() == 3 ? visit(ctx.expression(2)) : null;

    try {
      if (right1 instanceof String key) {
        return wtalLogicPort.applyKeyAccessor(left, key);
      } else if (right1 instanceof Integer from) {
        if (right2 == null) {
          return wtalLogicPort.applyNumericAccessor(left, from, null);
        } else if (right2 instanceof Integer to) {
          return wtalLogicPort.applyNumericAccessor(left, from, to);
        }
      }
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }

    throw new WtalIllegalOperationException(
        "Invalid access operation for object of type '%s'".formatted(left.getClass().getSimpleName()));
  }

  @Override
  public Function<Object, Object> visitLambda_expression(Lambda_expressionContext ctx) {
    var localVar = ctx.IDENTIFIER().getText();
    var expressionStr = expressionToString(ctx.expression());

    return new Function<Object, Object>() {
      @Override
      public Object apply(Object t) {
        var localContext = new HashMap<>(variableContext);
        localContext.put(localVar, t);

        return new AggregatorWtalEvaluator(wtalLogicPort, localContext).evaluate(expressionStr + ";");
      }
    };
  }

  @Override
  public Object visitFilterFunction(FilterFunctionContext ctx) {
    var left = visit(ctx.expression());
    var right = visitLambda_expression(ctx.lambda_expression());

    try {
      return wtalLogicPort.applyFilterFunction(left, right);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  @Override
  public Object visitMapFunction(MapFunctionContext ctx) {
    var left = visit(ctx.expression());
    var right = visitLambda_expression(ctx.lambda_expression());

    try {
      return wtalLogicPort.applyMapFunction(left, right);
    } catch (IllegalArgumentException e) {
      throw new WtalIllegalOperationException(e);
    }
  }

  private String expressionToString(ExpressionContext expression) {
    var strBuilder = new StringBuilder();

    expression.children.forEach(child -> {
      if (!(child instanceof ExpressionContext) || child.getChildCount() == 0) {
        strBuilder.append(child.getText()).append(" ");
      } else if (child instanceof ExpressionContext e) {
        strBuilder.append(expressionToString(e)).append(" ");
      }
    });

    return strBuilder.toString();
  }

  @Override
  public Object visitIsNullFunction(IsNullFunctionContext ctx) {
    var toCheck = visit(ctx.expression());

    return wtalLogicPort.applyIsNull(toCheck);
  }

  @Override
  public Object visitTernaryFunction(TernaryFunctionContext ctx) {
    var toCheck = visit(ctx.expression(0));
    var onOk = visit(ctx.expression(1));
    var onNok = visit(ctx.expression(2));

    return wtalLogicPort.applyTertiary(toCheck, onOk, onNok);
  }

}
