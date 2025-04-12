package ru.yandex.practicum.telemetry.collector.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KafkaTopicsConfig {

    @Value("${spring.kafka.topics.sensor-events}")
    private String sensorEventsTopic;

    @Value("${spring.kafka.topics.hubs-events}")
    private String hubsEventsTopic;

}
