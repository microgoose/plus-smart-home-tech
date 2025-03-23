package ru.yandex.practicum.telemetry.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.enums.SensorEventType;

import java.time.Instant;

@Getter
@Setter
@ToString
public abstract class HubEvent {
    private String id;
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
