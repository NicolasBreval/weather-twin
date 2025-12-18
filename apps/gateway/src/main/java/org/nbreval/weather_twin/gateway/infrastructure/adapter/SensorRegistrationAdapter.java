package org.nbreval.weather_twin.gateway.infrastructure.adapter;

import org.nbreval.weather_twin.gateway.application.entity.SensorRegistration;
import org.nbreval.weather_twin.gateway.application.port.in.SchedulerPort;
import org.nbreval.weather_twin.gateway.application.port.in.SensorConfigurationPort;
import org.nbreval.weather_twin.gateway.infrastructure.dto.UpdateExpressionDto;
import org.nbreval.weather_twin.gateway.infrastructure.dto.UpdateIntervalsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Web controller used to allow user to configure the system, like add new
 * sensors to process, unregister them, modify intervals to process of a
 * sensor,...
 */
@RestController
@RequestMapping("/registration")
@Tag(name = "Registration", description = "Registration and configuration of sensors on system.")
@ApiResponses({
    @ApiResponse(responseCode = "500", description = "An uncontrolled exception has been thrown during a request processing.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
    @ApiResponse(responseCode = "400", description = "Any request parameter has an invalid format, like query parameters or body.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
})
public class SensorRegistrationAdapter {

  /**
   * Allows to configure sensor configurations, like WTAL expressions, or
   * intervals.
   */
  private final SensorConfigurationPort sensorConfigurator;

  /**
   * Allows to create or remove schedulers, based on interval modifications
   * performed by this adapter.
   */
  private final SchedulerPort scheduler;

  public SensorRegistrationAdapter(SensorConfigurationPort sensorConfigurator, SchedulerPort scheduler) {
    this.sensorConfigurator = sensorConfigurator;
    this.scheduler = scheduler;
  }

  @Operation(summary = "Registers new sensor on system.", description = "Adds a new sensor in aggregations database and create all resources required by the sensor, like aggregation entry in database, or schedulers.", responses = {
      @ApiResponse(responseCode = "201", description = "The sensor has been successfully registered.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
      @ApiResponse(responseCode = "409", description = "The sensor to register already exists on system.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
  })
  @PutMapping
  public Mono<ResponseEntity<String>> registerSensorConfiguration(
      @RequestBody @Valid SensorRegistration registration) {
    sensorConfigurator.registerSensor(registration.device(), registration.sensor(),
        registration.defaultValue(), registration.aggregationExpression(), registration.flushExpression(),
        registration.intervals(), registration.sensorType(), registration.magnitude(), registration.description());

    // Register intervals
    registration.intervals().forEach(interval -> scheduler.schedule(interval));

    return Mono.just(new ResponseEntity<>("The sensor has been successfully registered.", HttpStatus.CREATED));
  }

  @Operation(summary = "Unregisters a sensor's interval from system.", description = "Removes the sensor interval information from all databases and modifies the schedulers if is necessary.", responses = {
      @ApiResponse(responseCode = "200", description = "The sensor interval has been unregister successfully.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
      @ApiResponse(responseCode = "404", description = "The sensor interval to unregister doesn't exist.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
  })
  @DeleteMapping("/{device}/{sensor}/{interval}")
  public Mono<ResponseEntity<String>> unregisterSensorConfiguration(@PathVariable("device") String device,
      @PathVariable("sensor") String sensor, @PathVariable("interval") long interval) {
    var intervalEmpty = sensorConfigurator.unregisterSensor(device, sensor, interval);

    if (intervalEmpty) {
      scheduler.unschedule(interval);
    }

    return Mono.just(new ResponseEntity<>("Sensor interval successfully unregistered.", HttpStatus.OK));
  }

  @Operation(summary = "Unregisters completely a sensor from the system.", description = "Removes the sensor information from all databases and modifies the schedulers if is necessary.", responses = {
      @ApiResponse(responseCode = "200", description = "The sensor interval has been unregister successfully.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
      @ApiResponse(responseCode = "404", description = "The sensor to unregister doesn't exist.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
  })
  @DeleteMapping("/{device}/{sensor}")
  public Mono<ResponseEntity<String>> unregisterSensorConfiguration(@PathVariable("device") String device,
      @PathVariable("sensor") String sensor) {
    sensorConfigurator.unregisterSensor(device, sensor)
        .forEach(interval -> scheduler.unschedule(interval));

    return Mono.just(new ResponseEntity<>("Sensor successfully unregistered.", HttpStatus.OK));
  }

  @Operation(summary = "Updates the intervals related to a sensor.", description = "Creates the new intervals not already related to sensor, keeps the intervals present in the list to update and already registered on system and removes the intervals already registered and no present on update list. Also, modifies the schedulers based on intervals operations performed.", responses = {
      @ApiResponse(responseCode = "200", description = "Intervals has been successfully updated", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
      @ApiResponse(responseCode = "404", description = "There are no sensor with specified name registered on system.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
  })
  @PostMapping("/intervals")
  public Mono<ResponseEntity<String>> updateIntervals(@RequestBody UpdateIntervalsDto updateIntervals) {
    var scheduleUpdates = sensorConfigurator.updateIntervals(updateIntervals.device(), updateIntervals.sensor(),
        updateIntervals.intervals());

    scheduleUpdates.getT1().forEach(interval -> scheduler.unschedule(interval));
    scheduleUpdates.getT2().forEach(interval -> scheduler.schedule(interval));

    return Mono.just(new ResponseEntity<>("Intervals successfully updated.", HttpStatus.OK));
  }

  @Operation(summary = "Updates a WTAL expression related to a sensor.", description = "The WTAL expression is updated on system database. If the expression to update is the aggregation expressions, produces an aggregation release.", responses = {
      @ApiResponse(responseCode = "200", description = "The expression has been successfully updated", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
  })
  @PostMapping("/expression/{type}")
  public Mono<ResponseEntity<?>> updateAggregationExpression(@RequestBody UpdateExpressionDto updateExpression,
      @PathVariable("type") String expressionType) {

    if (expressionType.equals("aggregation")) {
      sensorConfigurator.updateAggregationExpression(updateExpression.device(), updateExpression.sensor(),
          updateExpression.expression());
    } else if (expressionType.equals("flush")) {
      sensorConfigurator.updateFlushExpression(updateExpression.device(), updateExpression.sensor(),
          updateExpression.expression());
    } else {
      throw new IllegalArgumentException(
          "Invalid type '%s', only 'aggregation' and 'flush' are valid".formatted(expressionType));
    }

    return Mono.just(new ResponseEntity<>("Expression successfully updated", HttpStatus.OK));
  }
}
