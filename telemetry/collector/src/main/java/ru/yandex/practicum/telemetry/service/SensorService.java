package ru.yandex.practicum.telemetry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.maper.SensorEventAvroMapper;
import ru.yandex.practicum.telemetry.model.sensor.SensorEvent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static ru.yandex.practicum.telemetry.config.KafkaTopicsConfig.SENSOR_EVENT_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final KafkaTemplate<String, SensorEventAvro> kafkaTemplate;

    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, SensorEventAvro>> future =
                kafkaTemplate.send(SENSOR_EVENT_TOPIC.getTopic(), sensorEventAvro.getHubId(), sensorEventAvro);

        future.whenComplete((result, ex) -> {
           if (Objects.nonNull(ex)) {
               log.error(ex.getMessage(), ex);
           }
        });
    }
}
