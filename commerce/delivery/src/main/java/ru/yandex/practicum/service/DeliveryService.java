package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void markDeliverySuccessful(UUID orderId);

    void markDeliveryFailed(UUID orderId);

    void markDeliveryPicked(UUID orderId);

    double calculateDeliveryCost(OrderDto orderDto);

}
