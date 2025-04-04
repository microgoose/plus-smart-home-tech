package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorSnapshotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    /**
     * Обновляет снапшот по новому событию.
     * Возвращает обновленный снапшот, если данные изменились.
     */
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();

        // Получаем снапшот хаба или создаем новый
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, key ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );

        // Получаем старое состояние сенсора (если есть)
        SensorStateAvro oldState = snapshot.getSensorsState().get(sensorId);

        // Если есть старое состояние, проверяем, нужно ли обновлять
        if (oldState != null) {
            if (!event.getTimestamp().isAfter(oldState.getTimestamp())) {
                log.info("Пропускаем событие: {} (старее или равно текущему)", event);
                return Optional.empty();
            }
            if (oldState.getData().equals(event.getPayload())) {
                log.info("Пропускаем событие: {} (данные не изменились)", event);
                return Optional.empty();
            }
        }

        // Обновляем состояние сенсора
        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.getSensorsState().put(sensorId, newState);
        snapshot.setTimestamp(event.getTimestamp());

        log.info("Обновили снапшот для хаба {}: {}", hubId, snapshot);

        return Optional.of(snapshot);
    }
}
