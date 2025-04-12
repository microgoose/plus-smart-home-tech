package ru.yandex.practicum.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorsSnapshotAvroDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SensorsSnapshotAvroDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
