package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class TemplatesAdapter {

  private final SensorConfigurationPort sensorConfigurator;

  public TemplatesAdapter(SensorConfigurationPort sensorConfigurator) {
    this.sensorConfigurator = sensorConfigurator;
  }

  @GetMapping("/")
  public Mono<String> home(Model model) {
    var allRegistrations = sensorConfigurator.getAllRegistrations();

    model.addAttribute("registrations", allRegistrations);
    return Mono.just("home");
  }

}
