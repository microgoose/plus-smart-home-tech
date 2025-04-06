package ru.yandex.practicum.telemetry.analyzer.service.hub;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScenarioRemovedEventService implements HubEventService {

    private final ScenarioRepository scenarioRepository;

    @Override
    public Class<?> getEventType() {
        return ScenarioRemovedEventService.class;
    }

    @Override
    @Transactional
    public void processEvent(HubEventAvro event) {
        log.debug("Обработка события удаления сценария");
        String scenarioName = ((ScenarioRemovedEventAvro) event.getPayload()).getName();
        Scenario scenario = scenarioRepository
                .findByHubIdAndName(event.getHubId(), scenarioName)
                .orElseThrow(() -> new EntityNotFoundException("Сценарий " + scenarioName + " не найден"));

        scenarioRepository.delete(scenario);
        log.debug("Удалён сценарий {}", scenario);
    }
}