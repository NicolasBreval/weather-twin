package org.nbreval.weather_twin.gateway.infrastructure.config;

import java.util.stream.Collectors;

import org.nbreval.weather_twin.gateway.infrastructure.exception.AlreadyExistsException;
import org.nbreval.weather_twin.gateway.infrastructure.exception.NotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Handler object used to catch all exceptions thrown in web controller's
 * methods and automatically send a ResponseEntity with a specified HTTP
 * response code.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<?>> handleException(Exception e, ServerWebExchange exchange) {

    var request = exchange.getRequest();
    var uri = request.getURI().getPath();
    var queryParams = request.getQueryParams();

    HttpStatus code;
    String body = e.getMessage();
    String logMessage;

    if (e instanceof IllegalArgumentException) {
      code = HttpStatus.BAD_REQUEST;
      logMessage = "Bad request exception handled";
    } else if (e instanceof AlreadyExistsException) {
      code = HttpStatus.CONFLICT;
      logMessage = "Conflict exception handled";
    } else if (e instanceof NotExistsException) {
      code = HttpStatus.NOT_FOUND;
      logMessage = "Not found exception handled";
    } else {
      code = HttpStatus.INTERNAL_SERVER_ERROR;
      body = "Fatal error produced on service";
      logMessage = "Internal server exception handled";
    }

    var formattedParams = queryParams.entrySet().stream()
        .flatMap(p -> p.getValue().stream().map(v -> "%s=%s".formatted(p.getKey(), v)))
        .collect(Collectors.joining("&"));
    var formattedRequestInfo = uri + (formattedParams.isEmpty() ? "" : "?" + formattedParams);

    logger.error("%s for request '%s'".formatted(logMessage, formattedRequestInfo), e);

    return Mono.just(new ResponseEntity<>(body, code));
  }
}
