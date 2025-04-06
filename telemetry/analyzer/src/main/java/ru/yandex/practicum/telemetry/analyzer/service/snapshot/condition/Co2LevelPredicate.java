package ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Service
@RequiredArgsConstructor
public class Co2LevelPredicate implements ConditionPredicate {

    private final ConditionEvaluator evaluator;

    public boolean test(Object sensorData, Condition condition) {
        if (sensorData instanceof ClimateSensorAvro data)
            return evaluator.evaluate(data.getCo2Level(), condition.getOperation(), condition.getValue());
        return false;
    }

    public ConditionTypeAvro getConditionType() {
        return ConditionTypeAvro.CO2LEVEL;
    }

}
