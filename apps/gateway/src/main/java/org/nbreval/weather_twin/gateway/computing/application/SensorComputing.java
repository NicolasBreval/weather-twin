package org.nbreval.weather_twin.gateway.computing.application;

// ... (imports remain the same)

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.nbreval.weather_twin.gateway.computing.domain.Triple;
import org.nbreval.weather_twin.gateway.shared.MeanMeasureEvent;
import org.nbreval.weather_twin.gateway.shared.SensorNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Component used to receive all messages obtained from gateway module and
 * compute mean values. This is used to don't overload the server. The user can
 * configures the period used to compute the mean values, and configure multiple
 * periods at same time, using "computing.batching.window-list" property; by
 * example: <code>computing.batching.window-list=1m,5m,10m</code>.
 */
@Component
public class SensorComputing implements DisposableBean {
    /** Logger used to show some log lines in application terminal */
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorComputing.class);

    /** List of configured periods to compute means */
    private final List<Long> windowSizeList;

    /** MapDB instance used to store sum values of each event received from devices */
    private final DB db;

    /** Map created from MapDB instance to store device's aggregated values */
    private final ConcurrentMap<String, Triple<Long, Long, Double>> measuresAggregation;

    /** List of intervals which compute the means for each configured period */
    private final List<Disposable> scheduledTasks;

    @SuppressWarnings("unchecked")
    public SensorComputing(
            ApplicationEventPublisher eventPublisher,
            @Value("${computing.batching.window-list}") String[] windowSizeStrArray) {
        
        if (windowSizeStrArray == null || windowSizeStrArray.length == 0) {
            throw new IllegalArgumentException(
                    "Invalid value for 'computing.batching.window-list' property, it must contains, at least, one window size");
        }

        windowSizeList = Arrays.stream(windowSizeStrArray).map(w -> {
            if (!w.matches("\\d+(?:s|m)")) {
                throw new RuntimeException("Invalid window size configuration '%s'".formatted(w));
            }

            if (w.endsWith("s")) {
                return Duration.ofSeconds(Long.parseLong(w.substring(0, w.length() - 1))).toMillis();
            } else {
                return Duration.ofMinutes(Long.parseLong(w.substring(0, w.length() - 1))).toMillis();
            }
        }).toList();

        db = DBMaker.tempFileDB().transactionEnable().closeOnJvmShutdown().make();
        measuresAggregation = db.hashMap("store").keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA)
                .createOrOpen();
        scheduledTasks = windowSizeList.stream()
                .map(windowSize -> Flux.interval(Duration.ofMillis(windowSize))
                        .publishOn(Schedulers.boundedElastic())
                        .doOnNext(_ -> {
                            LOGGER.debug("Obtaining means for window size '%s'".formatted(windowSize));

                            var keysToRemove = measuresAggregation.keySet().stream()
                                    .filter(key -> key.startsWith(String.valueOf(windowSize) + "@"))
                                    .toList();

                            keysToRemove.forEach(key -> {
                                var aggregated = measuresAggregation.get(key);
                                var parts = key.split("@");
                                var deviceId = parts[1];
                                var sensorId = parts[2];
                                var value = aggregated.getRight() / aggregated.getMiddle();
                                var timestamp = aggregated.getLeft();

                                var event = new MeanMeasureEvent(this, deviceId, sensorId, timestamp, value,
                                        windowSize);
                                LOGGER.debug("Sending new MeanMeasureEvent: '%s'".formatted(event));
                                eventPublisher.publishEvent(event);
                            });

                            keysToRemove.forEach(measuresAggregation::remove);
                        }).subscribe())
                .toList();
    }

    /**
     * Receives all SensorNotification events from gateway module and stores the value in aggregations map
     * @param notification Event received from gateway
     */
    @EventListener
    public void listenForSensorData(SensorNotification notification) {
        LOGGER.debug("Received notification from '%s/%s/%d' with measure '%f'".formatted(notification.getDeviceId(),
                notification.getSensorId(), notification.getUtcTimestamp(), notification.getMeasure()));

        windowSizeList.forEach(ws -> {
            var combinedKey = "%d@%s@%s".formatted(ws, notification.getDeviceId(), notification.getSensorId());

            measuresAggregation.compute(combinedKey, (_, old) -> {
                if (old == null) {
                    return Triple.of(notification.getUtcTimestamp(), 1L, (double) notification.getMeasure());
                } else {
                    return old.set(l -> l == 0 ? notification.getUtcTimestamp() : l, l -> l + 1,
                            r -> r + notification.getMeasure());
                }
            });
        });
    }

    @Override
    public void destroy() throws Exception {
        scheduledTasks.forEach(Disposable::dispose);
    }
}