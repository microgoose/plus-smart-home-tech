package ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

@Component
public class ConditionEvaluator {

    public boolean evaluate(int sensorValue, ConditionOperationAvro operation, Integer targetValue) {
        if (targetValue == null) {
            return false;
        }

        return switch (operation) {
            case EQUALS -> sensorValue == targetValue;
            case GREATER_THAN -> sensorValue > targetValue;
            case LOWER_THAN -> sensorValue < targetValue;
        };
    }

}
