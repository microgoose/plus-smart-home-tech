package ru.yandex.practicum.telemetry.analyzer.processing;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.List;

@Slf4j
public class ConsumerProcessor<T> implements Runnable {

    private final List<String> topics;
    private final KafkaConsumer<String, T> consumer;
    private final EventProcessor<T> eventProcessor;
    private volatile boolean running = true;

    public ConsumerProcessor(List<String> topics,
                             KafkaConsumer<String, T> consumer,
                             EventProcessor<T> eventProcessor) {
        this.topics = topics;
        this.consumer = consumer;
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void run() {
        consumer.subscribe(topics);
        log.info("Подписались на топик: {}", topics);

        try {
            while (running) {
                ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(1000));

                if (records.isEmpty()) continue;

                for (ConsumerRecord<String, T> record : records) {
                    eventProcessor.process(record);
                }

                consumer.commitSync();
            }
        } catch (WakeupException e) {
            log.info("Консьюмер был остановлен");
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из {}", topics, e);
        } finally {
            shutdown();
        }
    }

    public void stop() {
        running = false;
        consumer.wakeup();
    }

    @PreDestroy
    private void shutdown() {
        try {
            consumer.commitSync();
        } finally {
            log.info("Закрываем консьюмер...");
            consumer.close();
        }
    }

}
