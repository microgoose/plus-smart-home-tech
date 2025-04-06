package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.processing.ConsumerProcessor;
import ru.yandex.practicum.telemetry.analyzer.processing.HubEventProcessor;
import ru.yandex.practicum.telemetry.analyzer.processing.SnapshotProcessor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ConsumerProcessorContext {

    private final KafkaConsumerConfig kafkaConsumerConfig;

    @Bean
    public ConsumerProcessor<HubEventAvro> hubConsumerProcessor(
            KafkaConsumer<String, HubEventAvro> consumer, HubEventProcessor processor) {
        return new ConsumerProcessor<>(
                List.of(kafkaConsumerConfig.getHubConsumer().getTopic()),
                consumer,
                processor);
    }

    @Bean
    public ConsumerProcessor<SensorsSnapshotAvro> snapshotConsumerProcessor(
            KafkaConsumer<String, SensorsSnapshotAvro> consumer, SnapshotProcessor processor) {
        return new ConsumerProcessor<>(
                List.of(kafkaConsumerConfig.getSnapshotConsumer().getTopic()),
                consumer,
                processor);
    }

}
