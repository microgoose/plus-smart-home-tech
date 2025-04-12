package ru.yandex.practicum.telemetry.collector.maper;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

public class SensorEventAvroMapper {

    public static SensorEventAvro mapSensorEvent(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(mapPayload(event))
                .build();
    }

    private static Object mapPayload(SensorEventProto event) {
        return switch (event.getPayloadCase()) {
            case TEMPERATURE_SENSOR_EVENT -> mapPayload(event.getTemperatureSensorEvent());
            case LIGHT_SENSOR_EVENT -> mapPayload(event.getLightSensorEvent());
            case CLIMATE_SENSOR_EVENT -> mapPayload(event.getClimateSensorEvent());
            case SWITCH_SENSOR_EVENT -> mapPayload(event.getSwitchSensorEvent());
            case MOTION_SENSOR_EVENT -> mapPayload(event.getMotionSensorEvent());
            default ->
                    throw new IllegalArgumentException("Payload case not set: " + event.getPayloadCase());
        };
    }

    private static LightSensorAvro mapPayload(LightSensorProto event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    private static MotionSensorAvro mapPayload(MotionSensorProto event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();
    }

    private static SwitchSensorAvro mapPayload(SwitchSensorProto event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    private static ClimateSensorAvro mapPayload(ClimateSensorProto event) {
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getCo2Level())
                .setHumidity(event.getHumidity())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    private static TemperatureSensorAvro mapPayload(TemperatureSensorProto event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}
