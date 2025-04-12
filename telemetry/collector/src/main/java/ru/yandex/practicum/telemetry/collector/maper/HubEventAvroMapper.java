package ru.yandex.practicum.telemetry.collector.maper;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

public class HubEventAvroMapper {

    public static HubEventAvro mapSensorEvent(HubEventProto event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(mapPayload(event))
                .build();
    }

    private static Object mapPayload(HubEventProto event) {
        return switch (event.getPayloadCase()) {
            case DEVICE_ADDED -> mapPayload(event.getDeviceAdded());
            case DEVICE_REMOVED -> mapPayload(event.getDeviceRemoved());
            case SCENARIO_ADDED -> mapPayload(event.getScenarioAdded());
            case SCENARIO_REMOVED -> mapPayload(event.getScenarioRemoved());
            default -> throw new IllegalArgumentException("Payload case not set: " + event.getPayloadCase());
        };
    }

    private static DeviceAddedEventAvro mapPayload(DeviceAddedEventProto event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getType().name()))
                .build();
    }

    private static DeviceRemovedEventAvro mapPayload(DeviceRemovedEventProto event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private static ScenarioAddedEventAvro mapPayload(ScenarioAddedEventProto event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setActions(mapActions(event.getActionList()))
                .setConditions(mapConditions(event.getConditionList()))
                .setName(event.getName())
                .build();
    }

    private static ScenarioRemovedEventAvro mapPayload(ScenarioRemovedEventProto event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    private static List<DeviceActionAvro> mapActions(List<DeviceActionProto> deviceActions) {
        return deviceActions.stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                        .setSensorId(deviceAction.getSensorId())
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
    }

    private static List<ScenarioConditionAvro> mapConditions(List<ScenarioConditionProto> scenarioConditions) {
        return scenarioConditions.stream()
                .map(HubEventAvroMapper::mapCondition)
                .toList();
    }

    private static ScenarioConditionAvro mapCondition(ScenarioConditionProto scenarioCondition) {
        Object value = switch (scenarioCondition.getValueCase()) {
            case BOOL_VALUE -> scenarioCondition.getBoolValue();
            case INT_VALUE -> scenarioCondition.getIntValue();
            case VALUE_NOT_SET -> null;
        };

        return ScenarioConditionAvro.newBuilder()
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(value)
                .build();
    }

}
