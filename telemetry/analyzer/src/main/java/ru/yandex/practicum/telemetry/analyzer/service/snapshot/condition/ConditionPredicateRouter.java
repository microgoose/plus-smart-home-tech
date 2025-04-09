package ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConditionPredicateRouter {

    private final Map<ConditionTypeAvro, ConditionPredicate> processors;

    @Autowired
    public ConditionPredicateRouter(List<ConditionPredicate> eventProcessors) {
        processors = eventProcessors.stream()
                .collect(Collectors.toMap(ConditionPredicate::getConditionType, Function.identity()));
    }

    public ConditionPredicate getProcessor(ConditionTypeAvro type) {
        for (Map.Entry<ConditionTypeAvro, ConditionPredicate> entry : processors.entrySet()) {
            if (entry.getKey().equals(type)) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("Не найден обработчик условия сценария: " + type);
    }

}
