package ru.yandex.practicum.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class SensorSnapshotKafkaProducer {

    private final KafkaProducer<String, SensorsSnapshotAvro> producer;

    @Autowired
    public SensorSnapshotKafkaProducer(AggregatorKafkaConfig config) {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProducerKeySerializer());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProducerValueSerializer());
        producer = new KafkaProducer<>(producerConfig);
    }

    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> getProducer() {
        return producer;
    }
}
