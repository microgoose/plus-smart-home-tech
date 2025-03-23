package ru.yandex.practicum.telemetry.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.enums.SensorEventType;

@Getter
@Setter
@ToString
public class ClimateSensorEvent extends SensorEvent {
    private int temperatureC;
    private int humidity;
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}