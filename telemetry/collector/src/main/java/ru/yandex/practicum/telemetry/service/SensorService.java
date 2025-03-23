package ru.yandex.practicum.telemetry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.maper.SensorEventAvroMapper;
import ru.yandex.practicum.telemetry.model.sensor.SensorEvent;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final KafkaTemplate<String, SensorEventAvro> kafkaTemplate;

    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, SensorEventAvro>> future =
                kafkaTemplate.send("telemetry.sensors.v1", sensorEventAvro.getHubId(), sensorEventAvro);

        future.whenComplete((result, ex) -> {
           if (ex != null) {
               log.error(ex.getMessage(), ex);
           }
        });
    }
}
