package ru.yandex.practicum.telemetry.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.telemetry.analyzer.processing.ConsumerProcessor;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Analyzer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);

        // Получаем конкретные ConsumerProcessor'ы, а не EventProcessor'ы
        ConsumerProcessor<?> hubConsumerProcessor =
                context.getBean("hubConsumerProcessor", ConsumerProcessor.class);
        ConsumerProcessor<?> snapshotConsumerProcessor =
                context.getBean("snapshotConsumerProcessor", ConsumerProcessor.class);

        // Запускаем каждый в своем потоке
        Thread hubThread = new Thread(hubConsumerProcessor, "HubEventConsumerThread");
        Thread snapshotThread = new Thread(snapshotConsumerProcessor, "SnapshotConsumerThread");

        hubThread.start();
        snapshotThread.run();
    }

}