package ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public interface ConditionPredicate {

    boolean test(Object sensorData, Condition condition);

    ConditionTypeAvro getConditionType();

}
