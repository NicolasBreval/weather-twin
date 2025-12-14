package org.nbreval.weather_twin.gateway.infrastructure.util;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class AggregatorWtalErrorListener extends BaseErrorListener {

  private final StringBuilder errors;

  public AggregatorWtalErrorListener() {
    this.errors = new StringBuilder();
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
      String msg, RecognitionException e) {
    var error = "Syntax error at line %d:%d -> %s%n".formatted(line, charPositionInLine, msg);
    errors.append(error);
  }

  public String getErrors() {
    return errors.toString();
  }

  public boolean hasErrors() {
    return errors.length() > 0;
  }

}
