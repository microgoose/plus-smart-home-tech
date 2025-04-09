package ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Service
@RequiredArgsConstructor
public class MotionPredicate implements ConditionPredicate {

    private final ConditionEvaluator evaluator;

    public boolean test(Object sensorData, Condition condition) {
        if (sensorData instanceof MotionSensorAvro data)
            return evaluator.evaluate(data.getMotion() ? 1 : 0, condition.getOperation(), condition.getValue());
        return false;
    }

    public ConditionTypeAvro getConditionType() {
        return ConditionTypeAvro.MOTION;
    }

}
