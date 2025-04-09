package ru.yandex.practicum.telemetry.collector.service;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.util.RandomUtil;

import java.time.Instant;

@Service
@Slf4j
public class EventDataProducer {

    @GrpcClient("collector")
    private CollectorControllerGrpc.CollectorControllerBlockingStub collectorStub;

//    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    public void sendEvent() {
        SensorEventProto event = createTestMotionSensorEvent();

        try {
            log.debug("Отправляю данные: {}", event);
            GeneratedMessageV3 response = collectorStub.collectSensorEvent(event);
            log.debug("Получил ответ от коллектора: {}", response);
        } catch (Exception e) {
            log.error("Error while", e);
        }
    }

    public static SensorEventProto createTestMotionSensorEvent() {
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
        MotionSensorProto motionSensor = MotionSensorProto.newBuilder()
                .setLinkQuality(85)
                .setMotion(true)
                .setVoltage(RandomUtil.getRandomValue("voltage", 50))
                .build();

        return SensorEventProto.newBuilder()
                .setId("sensor-123")
                .setTimestamp(timestamp)
                .setHubId("hub-001")
                .setMotionSensorEvent(motionSensor)
                .build();
    }

}