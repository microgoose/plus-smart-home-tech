package ru.yandex.practicum.telemetry.analyzer.processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.service.hub.HubEventProcessorRouter;
import ru.yandex.practicum.telemetry.analyzer.service.hub.HubEventService;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubEventProcessor implements EventProcessor<HubEventAvro> {

    private final HubEventProcessorRouter hubEventProcessorRouter;

    @Override
    public void process(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro event = record.value();
        log.debug("Получено событие хаба: {}", event);

        try {
            HubEventService hubEventService = hubEventProcessorRouter.getProcessor(event);
            hubEventService.processEvent(event);
        } catch (Exception ex) {
            log.error("Ошибка при обработке события хаба", ex);
        }
    }

}
