package ru.yandex.practicum.telemetry.analyzer.processing;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventProcessor<T> {
    void process(ConsumerRecord<String, T> record) throws Exception;
}
