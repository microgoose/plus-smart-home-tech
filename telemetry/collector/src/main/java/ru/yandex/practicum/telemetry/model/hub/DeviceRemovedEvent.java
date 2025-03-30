package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.enums.HubEventType;

@Getter
@Setter
public class DeviceRemovedEvent extends HubEvent {
    private String id;

    public DeviceRemovedEvent(String id, String hubId) {
        this.id = id;
        this.hubId = hubId;
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
