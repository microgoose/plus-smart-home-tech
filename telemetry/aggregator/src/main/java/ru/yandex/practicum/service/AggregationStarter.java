package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.AggregatorKafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final AggregatorKafkaConfig kafkaConfig;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final SensorSnapshotService sensorSnapshotService;
    private volatile boolean running = true;

    public void start() {
        try {
            consumer.subscribe(List.of(kafkaConfig.getSensorTopic()));
            log.info("Подписались на топик: {}", kafkaConfig.getSensorTopic());

            while (running) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    processEvent(record);
                }

                if (!records.isEmpty()) {
                    consumer.commitSync();
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе агрегации", e);
        } finally {
            shutdown();
        }
    }

    private void processEvent(ConsumerRecord<String, SensorEventAvro> record) {
        SensorEventAvro event = record.value();
        log.info("Получено событие: {}", event);

        sensorSnapshotService.updateState(event)
                .ifPresent(snapshot -> {
                    log.info("Отправляем обновленный снапшот: {}", snapshot);
                    producer.send(new ProducerRecord<>(kafkaConfig.getSnapshotTopic(), snapshot.getHubId(), snapshot));
                });
    }

    public void stop() {
        running = false;
        consumer.wakeup();
    }

    private void shutdown() {
        try {
            producer.flush();
            consumer.commitSync();
        } finally {
            log.info("Закрываем консьюмер и продюсер...");
            consumer.close();
            producer.close();
        }
    }
}
