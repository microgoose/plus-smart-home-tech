package ru.yandex.practicum.telemetry.analyzer.service.hub;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.*;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScenarioAddedEventService implements HubEventService {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public Class<?> getEventType() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    @Transactional
    public void processEvent(HubEventAvro event) {
        log.debug("Обработка события добавления сценария");
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();

        Scenario scenario = scenarioRepository
                .findByHubIdAndName(event.getHubId(), payload.getName())
                .orElse(Scenario.builder()
                        .hubId(event.getHubId())
                        .name(payload.getName())
                        .conditions(new ArrayList<>())
                        .actions(new ArrayList<>())
                        .build());

        scenario.getConditions().addAll(mapScenarioConditions(scenario, event, payload));
        scenario.getActions().addAll(mapScenarioActions(scenario, event, payload));
        scenario = scenarioRepository.save(scenario);
        log.debug("Добавлен сценарий: {}", scenario);
    }

    private List<ScenarioCondition> mapScenarioConditions(Scenario scenario, HubEventAvro event,
                                                          ScenarioAddedEventAvro payload) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = payload.getConditions();
        List<ScenarioCondition> scenarioConditions = scenario.getConditions();

        for (ScenarioConditionAvro sca : scenarioConditionsAvro) {
            Sensor sensor = sensorRepository.findByIdAndHubId(sca.getSensorId(), event.getHubId())
                    .orElseThrow(() -> new IllegalStateException("Сенсор не найден: " + sca.getSensorId()));

            int value;

            if (sca.getValue() instanceof Boolean val) {
                value = val ? 1 : 0;
            } else if (sca.getValue() instanceof Integer val) {
                value = val;
            } else {
                continue;
            }

            Condition condition = Condition.builder()
                    .type(sca.getType())
                    .operation(sca.getOperation())
                    .value(value)
                    .build();

            conditionRepository.save(condition);

            ScenarioConditionId id = ScenarioConditionId.builder()
                    .conditionId(condition.getId())
                    .scenarioId(scenario.getId())
                    .sensorId(sensor.getId())
                    .build();

            scenarioConditions.add(ScenarioCondition.builder()
                    .id(id)
                    .scenario(scenario)
                    .sensor(sensor)
                    .condition(condition)
                    .build());
        }

        return scenarioConditions;
    }

    private List<ScenarioAction> mapScenarioActions(Scenario scenario, HubEventAvro event,
                                                    ScenarioAddedEventAvro payload) {
        List<DeviceActionAvro> deviceActionsAvro = payload.getActions();
        List<ScenarioAction> scenarioActions = scenario.getActions();

        for (DeviceActionAvro daa : deviceActionsAvro) {
            Sensor sensor = sensorRepository.findByIdAndHubId(daa.getSensorId(), event.getHubId())
                    .orElseThrow(() -> new IllegalStateException("Сенсор не найден: " + daa.getSensorId()));

            if (daa.getValue() == null)
                continue;

            Action action = Action.builder()
                    .type(daa.getType())
                    .value(daa.getValue())
                    .build();

            actionRepository.save(action);

            ScenarioActionId id = ScenarioActionId.builder()
                    .actionId(action.getId())
                    .scenarioId(scenario.getId())
                    .sensorId(sensor.getId())
                    .build();

            scenarioActions.add(ScenarioAction.builder()
                    .id(id)
                    .action(action)
                    .scenario(scenario)
                    .sensor(sensor)
                    .build());
        }

        return scenarioActions;
    }
}