package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.enums.HubEventType;

@Getter
@Setter
public class ScenarioRemovedEvent extends HubEvent {
    private String name;

    public ScenarioRemovedEvent(String hubId, String name) {
        this.hubId = hubId;
        this.name = name;
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
