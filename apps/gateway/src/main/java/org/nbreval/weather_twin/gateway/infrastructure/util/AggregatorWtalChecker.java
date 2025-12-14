package org.nbreval.weather_twin.gateway.infrastructure.util;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.nbreval.weather_twin.gateway.infrastructure.exception.WtalParsingException;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalLexer;
import org.nbreval.weather_twin.gateway.infrastructure.util.wtal.AggregatorWtalParser;

public class AggregatorWtalChecker {

  public static void checkExpression(String expression) {
    var stream = CharStreams.fromString(expression);
    var lexer = new AggregatorWtalLexer(stream);
    var tokens = new CommonTokenStream(lexer);

    var parser = new AggregatorWtalParser(tokens);

    var errorListener = new AggregatorWtalErrorListener();
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);

    parser.start();

    if (errorListener.hasErrors()) {
      throw new WtalParsingException(errorListener.getErrors());
    }
  }
}
