package ru.yandex.practicum.telemetry.analyzer.service.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HubEventProcessorRouter {
    private final Map<Class<?>, HubEventService> processors;

    @Autowired
    public HubEventProcessorRouter(List<HubEventService> eventProcessors) {
        processors = eventProcessors.stream()
                .collect(Collectors.toMap(HubEventService::getEventType, Function.identity()));
    }

    public HubEventService getProcessor(HubEventAvro event) {
        Object payload = event.getPayload();
        for (Map.Entry<Class<?>, HubEventService> entry : processors.entrySet()) {
            if (entry.getKey().isInstance(payload)) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("Не найден обработчик события: " + payload.getClass().getName());
    }
}
