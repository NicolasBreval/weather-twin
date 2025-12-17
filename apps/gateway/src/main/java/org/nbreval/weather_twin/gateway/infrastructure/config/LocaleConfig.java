package org.nbreval.weather_twin.gateway.infrastructure.config;

import java.util.Locale;

import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.i18n.LocaleContextResolver;

import reactor.util.context.Context;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleContextResolver localeContextResolver() {
    return new LocaleContextResolver() {

      private static final String COOKIE_NAME = "LANG_CHOICE";

      @Override
      public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(COOKIE_NAME);
        Locale locale = (cookie != null) ? Locale.forLanguageTag(cookie.getValue()) : Locale.getDefault();
        return new SimpleLocaleContext(locale);
      }

      @Override
      public void setLocaleContext(ServerWebExchange exchange, @Nullable LocaleContext localeContext) {
        if (localeContext != null && localeContext.getLocale() != null) {
          Locale locale = localeContext.getLocale();
          ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, locale.toLanguageTag())
              .path("/")
              .maxAge(java.time.Duration.ofDays(30))
              .build();
          exchange.getResponse().addCookie(cookie);
        }
      }
    };
  }

  @Bean
  public WebFilter localeChangeFilter(LocaleContextResolver localeResolver, MessageSource messageSource) {
    return (exchange, chain) -> {
      LocaleContext localeContext = localeResolver.resolveLocaleContext(exchange);

      var locale = localeContext.getLocale();
      String mensajePrueba = messageSource.getMessage("site.header.title", null, "NO ENCONTRADO", locale);
      System.out.println("Idioma detectado: " + locale);
      System.out.println("Mensaje resuelto: " + mensajePrueba);

      return chain.filter(exchange)
          .contextWrite(Context.of(LocaleContext.class, localeContext));
    };
  }

  @Bean
  public MessageSource messageSource() {
    var messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
