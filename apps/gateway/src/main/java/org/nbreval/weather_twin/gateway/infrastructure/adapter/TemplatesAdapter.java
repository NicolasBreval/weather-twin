package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import java.util.List;
import java.util.regex.Pattern;

import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.nbreval.weather_twin.gateway.infrastructure.exception.NotExistsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;

@Controller
public class TemplatesAdapter {

  private static final String validToastTypePattern = "info|success|warning|error";

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

  @GetMapping("/sensor/items")
  public Mono<String> newAddIntervalItem(@RequestParam("device") String device, @RequestParam("sensor") String sensor,
      Model model) {
    var allSensors = sensorConfigurator.getAllRegistrations();
    model.addAttribute("r",
        allSensors.stream().filter(s -> s.device().equals(device) && s.sensor().equals(sensor)).findFirst().orElseThrow(
            () -> new NotExistsException("Does not exist any sensor for '%s.%s'".formatted(device, sensor))));
    model.addAttribute("i", allSensors.size());
    return Mono.just("fragments/components :: sensor-item");
  }

  @GetMapping("/toast")
  public Mono<String> getToast(@RequestParam("type") String type, @RequestParam("message") String message,
      Model model) {
    if (!type.matches(validToastTypePattern)) {
      throw new IllegalArgumentException("Invalid toast type '%s'".formatted(type));
    }

    model.addAttribute("msg", message);
    model.addAttribute("type", type);
    return Mono.just("fragments/components :: toast");
  }

  @GetMapping("/interval")
  public Mono<String> getIntervalItem() {
    return Mono.just("fragments/components :: interval-item-input");
  }

}
