package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.enums.HubEventType;

import java.util.List;

@Getter
@Setter
public class ScenarioAddedEvent extends HubEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    public ScenarioAddedEvent(String hubId, String name, List<ScenarioCondition> conditions, List<DeviceAction> actions) {
        this.hubId = hubId;
        this.name = name;
        this.conditions = conditions;
        this.actions = actions;
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
