package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

@Entity
@Table(name = "conditions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ConditionTypeAvro type;

    @Column(name = "operation")
    @Enumerated(value = EnumType.STRING)
    private ConditionOperationAvro operation;

    @Column(name = "value")
    private Integer value;
}
