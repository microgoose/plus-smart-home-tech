package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Data;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "analyzer.kafka")
@Data
public class KafkaConsumerConfig {

    private ConsumerConfig hubConsumer;
    private ConsumerConfig snapshotConsumer;

    @Data
    public static class ConsumerConfig {
        private Properties properties;
        private String topic;
    }

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubConsumer() {
        Properties props = hubConsumer.getProperties();
        return new KafkaConsumer<>(props);
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer() {
        Properties props = snapshotConsumer.getProperties();
        return new KafkaConsumer<>(props);
    }
}