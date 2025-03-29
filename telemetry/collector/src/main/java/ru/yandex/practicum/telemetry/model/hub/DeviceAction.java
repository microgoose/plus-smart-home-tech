package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.enums.ActionType;

@Getter
@Setter
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
