package ru.yandex.practicum.telemetry.analyzer.service.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventService {

    Class<?> getEventType();

    void processEvent(HubEventAvro event);

}

