package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.telemetry.enums.DeviceType;
import ru.yandex.practicum.telemetry.enums.HubEventType;

@Getter
@Setter
@NoArgsConstructor
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    public DeviceAddedEvent(String id, String hubId, DeviceType deviceType) {
        this.id = id;
        this.hubId = hubId;
        this.deviceType = deviceType;
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
