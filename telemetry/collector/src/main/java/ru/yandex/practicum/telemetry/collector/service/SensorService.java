package ru.yandex.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaTopicsConfig;
import ru.yandex.practicum.telemetry.collector.maper.SensorEventAvroMapper;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final KafkaTopicsConfig topicsConfig;
    private final KafkaTemplate<String, SensorEventAvro> kafkaTemplate;

    public void collectSensorEvent(SensorEventProto event) {
        SensorEventAvro sensorEventAvro = SensorEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, SensorEventAvro>> future =
                kafkaTemplate.send(topicsConfig.getSensorEventsTopic(), event.getHubId(), sensorEventAvro);

        future.whenComplete((result, ex) -> {
           if (Objects.nonNull(ex)) {
               log.error(ex.getMessage(), ex);
           }
        });
    }
}
