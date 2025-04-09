package ru.yandex.practicum.telemetry.analyzer.service.hub;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceAddedEventService implements HubEventService {

    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getEventType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    @Transactional
    public void processEvent(HubEventAvro event) {
        log.debug("Обработка события добавления устройства");
        DeviceAddedEventAvro deviceAddedPayload = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceAddedPayload.getId())
                .hubId(event.getHubId())
                .build();

        //todo исключение при обновлении?
        sensorRepository.save(sensor);
    }
}