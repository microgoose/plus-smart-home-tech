package ru.yandex.practicum.telemetry.analyzer.service.hub;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceRemovedEventService implements HubEventService {

    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getEventType() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    @Transactional
    public void processEvent(HubEventAvro event) {
        log.debug("Обработка события удаления устройства");
        DeviceRemovedEventAvro deviceRemovedPayload = (DeviceRemovedEventAvro) event.getPayload();

        sensorRepository.findByIdAndHubId(deviceRemovedPayload.getId(), event.getHubId())
                        .ifPresent(sensorRepository::delete);
    }
}