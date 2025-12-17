package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.net.URI;
import java.util.Locale;

import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/lang")
public class LocaleController {

  private final LocaleContextResolver localeContextResolver;

  public LocaleController(LocaleContextResolver localeContextResolver) {
    this.localeContextResolver = localeContextResolver;
  }

  @GetMapping("/")
  public Mono<Void> changeLanguage(@RequestParam("lang") String lang, ServerWebExchange exchange) {
    localeContextResolver.setLocaleContext(exchange, new SimpleLocaleContext(Locale.forLanguageTag(lang)));

    String referer = exchange.getRequest().getHeaders().getFirst("Referer");
    String redirectUrl = (referer != null) ? referer : "/";

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.SEE_OTHER);
    response.getHeaders().setLocation(URI.create(redirectUrl));

    return response.setComplete();
  }

}
