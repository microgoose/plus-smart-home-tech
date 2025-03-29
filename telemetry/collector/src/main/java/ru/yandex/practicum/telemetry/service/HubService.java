package ru.yandex.practicum.telemetry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.maper.HubEventAvroMapper;
import ru.yandex.practicum.telemetry.model.hub.HubEvent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static ru.yandex.practicum.telemetry.config.KafkaTopicsConfig.HUB_EVENT_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubService {

    private final KafkaTemplate<String, HubEventAvro> kafkaTemplate;

    public void collectHubEvent(HubEvent event) {
        HubEventAvro hubEventAvro = HubEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, HubEventAvro>> future =
                kafkaTemplate.send(HUB_EVENT_TOPIC.getTopic(), event.getType().name(), hubEventAvro);

        future.whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

}
