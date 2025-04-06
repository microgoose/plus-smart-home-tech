package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import com.google.protobuf.Timestamp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.model.*;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition.ConditionPredicate;
import ru.yandex.practicum.telemetry.analyzer.service.snapshot.condition.ConditionPredicateRouter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapshotEventService {

    private final ScenarioRepository scenarioRepository;
    private final ConditionPredicateRouter conditionRouter;

    @Transactional
    public List<DeviceActionRequest> makeRequestsFromSnapshot(SensorsSnapshotAvro snapshot) {
        log.debug("Начинаем обработку снапшота");
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        log.debug("Найдено {} сценариев для хаба {}, на момент {}",
                scenarios.size(), snapshot.getHubId(), snapshot.getTimestamp());

        List<DeviceActionRequest> requests = new ArrayList<>();

        for (Scenario scenario : scenarios) {
            if (checkConditions(scenario, snapshot)) {
                requests.addAll(makeActions(scenario, snapshot));
            }
        }

        return requests;
    }

    private boolean checkConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (ScenarioCondition sc : scenario.getConditions()) {
            SensorStateAvro sensorPayload = snapshot.getSensorsState().get(sc.getSensor().getId());
            Condition condition = sc.getCondition();
            ConditionPredicate evaluator = conditionRouter.getProcessor(condition.getType());

            if (sensorPayload == null) {
                log.warn("Сенсор {} не найден в снапшоте для сценария {}", sc.getSensor().getId(), scenario.getName());
                return false;
            }

            if (!evaluator.test(sensorPayload.getData(), condition)) {
                log.debug("Сценарий {} не прошёл проверку", scenario);
                return false;
            }
        }

        log.debug("Сценарий {} прошёл проверку", scenario);

        return true;
    }

    private List<DeviceActionRequest> makeActions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        List<DeviceActionRequest> requests = new ArrayList<>();

        Instant snapshotTimestamp = snapshot.getTimestamp();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(snapshotTimestamp.getEpochSecond())
                .setNanos(snapshotTimestamp.getNano())
                .build();

        for (ScenarioAction sa : scenario.getActions()) {
            Action action = sa.getAction();
            DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                    .setSensorId(sa.getSensor().getId())
                    .setType(ActionTypeProto.valueOf(action.getType().toString()))
                    .setValue(action.getValue())
                    .build();

            requests.add(DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(actionProto)
                    .setTimestamp(timestamp)
                    .build());
        }

        return requests;
    }

}
