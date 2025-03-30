package ru.yandex.practicum.telemetry.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.enums.SensorEventType;

@Getter
@Setter
@ToString
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    public LightSensorEvent(String id, String hubId, Integer linkQuality, Integer luminosity) {
        this.id = id;
        this.hubId = hubId;
        this.linkQuality = linkQuality;
        this.luminosity = luminosity;
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}