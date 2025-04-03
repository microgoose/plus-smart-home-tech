package ru.yandex.practicum.config;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Configuration
@Getter
public class SensorEventKafkaConsumer {

    private final KafkaConsumer<String, SensorEventAvro> consumer;

    @Autowired
    public SensorEventKafkaConsumer(AggregatorKafkaConfig config) {
        Properties consumerConfig = new Properties();
        consumerConfig.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        consumerConfig.setProperty(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumerGroupId());
        consumerConfig.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getConsumerKeyDeserializer());
        consumerConfig.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getConsumerValueDeserializer());
        consumerConfig.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getConsumerAutoOffsetReset());
        consumer = new KafkaConsumer<>(consumerConfig);
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> getConsumer() {
        return consumer;
    }

}
