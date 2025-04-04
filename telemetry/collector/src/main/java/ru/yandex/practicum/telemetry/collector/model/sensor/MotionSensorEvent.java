package ru.yandex.practicum.telemetry.collector.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    public MotionSensorEvent(String id, String hubId, Integer linkQuality, Boolean motion, Integer voltage) {
        this.id = id;
        this.hubId = hubId;
        this.linkQuality = linkQuality;
        this.motion = motion;
        this.voltage = voltage;
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}