package org.nbreval.weather_twin.gateway.computing.infrastructure.adapter.in;

import org.nbreval.weather_twin.gateway.computing.application.service.SensorComputingService;
import org.nbreval.weather_twin.gateway.shared.domain.model.SensorNotification;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Component used to receive all messages obtained from gateway module and
 * compute mean values. This is used to don't overload the server. The user can
 * configures the period used to compute the mean values, and configure multiple
 * periods at same time, using "computing.batching.window-list" property; by
 * example: <code>computing.batching.window-list=1m,5m,10m</code>.
 */
@Component
public class SensorComputingAdapter {

  private final SensorComputingService sensorComputingService;

  public SensorComputingAdapter(SensorComputingService sensorComputingService) {
    this.sensorComputingService = sensorComputingService;
  }

  @EventListener
  public void listenForSensorNotifications(SensorNotification notification) {
    sensorComputingService.processSensorNotification(notification);
  }

}