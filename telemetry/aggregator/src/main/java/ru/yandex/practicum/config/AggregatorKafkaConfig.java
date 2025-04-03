package ru.yandex.practicum.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AggregatorKafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.sensors}")
    private String sensorTopic;

    @Value("${kafka.topics.snapshots}")
    private String snapshotTopic;

    @Value("${kafka.producer.value-serializer}")
    private String producerValueSerializer;

    @Value("${kafka.producer.key-serializer}")
    private String producerKeySerializer;

    @Value("${kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${kafka.consumer.key-deserializer}")
    private String consumerKeyDeserializer;

    @Value("${kafka.consumer.value-deserializer}")
    private String consumerValueDeserializer;

    @Value("${kafka.consumer.auto-offset-reset}")
    private String consumerAutoOffsetReset;

}
