package ru.yandex.practicum.model;

import lombok.Getter;

@Getter
public enum DeliveryTariff {

    BASE_COST(5.0),
    ADDRESS_1_MULTIPLIER(1.0),
    ADDRESS_2_MULTIPLIER(2.0),
    FRAGILE_SURCHARGE_PERCENT(0.2),
    WEIGHT_MULTIPLIER(0.3),
    VOLUME_MULTIPLIER(0.2),
    DIFFERENT_STREET_SURCHARGE_PERCENT(0.2);

    private final double value;

    DeliveryTariff(double value) {
        this.value = value;
    }
}
