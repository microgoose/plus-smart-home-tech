package ru.yandex.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaTopicsConfig;
import ru.yandex.practicum.telemetry.collector.maper.HubEventAvroMapper;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubService {

    private final KafkaTopicsConfig topicsConfig;
    private final KafkaTemplate<String, HubEventAvro> kafkaTemplate;

    public void collectHubEvent(HubEventProto event) {
        HubEventAvro hubEventAvro = HubEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, HubEventAvro>> future =
                kafkaTemplate.send(topicsConfig.getHubsEventsTopic(), event.getHubId(), hubEventAvro);

        future.whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

}
