package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorSnapshotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();
        Instant eventTimestamp = event.getTimestamp();
        Object newPayload = event.getPayload();

        if (newPayload == null || !isSupportedSensorType(newPayload)) {
            log.warn("Пропущено событие с неподдерживаемым payload: {}", event);
            return Optional.empty();
        }

        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, key ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(eventTimestamp)
                        .setSensorsState(new ConcurrentHashMap<>())
                        .build()
        );

        SensorStateAvro previousState = snapshot.getSensorsState().get(sensorId);

        if (previousState != null) {
            boolean isPreviousNewer = previousState.getTimestamp().isAfter(eventTimestamp);
            boolean isDataSame = previousState.getData().equals(newPayload);

            if (isPreviousNewer || isDataSame) {
                log.debug("Пропускаем событие: {}",
                        isPreviousNewer ? "устаревший timestamp" : "данные не изменились");
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(eventTimestamp)
                .setData(newPayload)
                .build();
        snapshot.getSensorsState().put(sensorId, newState);

        snapshot.setTimestamp(eventTimestamp);

        log.info("Обновили снапшот для хаба {}: {}", hubId, snapshot);
        return Optional.of(snapshot);
    }

    private boolean isSupportedSensorType(Object payload) {
        return payload instanceof ClimateSensorAvro ||
                payload instanceof LightSensorAvro ||
                payload instanceof MotionSensorAvro ||
                payload instanceof SwitchSensorAvro ||
                payload instanceof TemperatureSensorAvro;
    }
}