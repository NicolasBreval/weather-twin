package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class TemplatesController {

  @GetMapping("/")
  public Mono<String> home(Model model) {
    return Mono.just("home");
  }

}
