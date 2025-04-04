package ru.yandex.practicum.telemetry.collector.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SwitchSensorEvent extends SensorEvent {
    private Boolean state;

    public SwitchSensorEvent(String id, String hubId, Boolean state) {
        this.id = id;
        this.hubId = hubId;
        this.state = state;
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
