package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    double calculateProductCost(OrderDto orderDto);

    double calculateTotalCost(OrderDto orderDto);

    PaymentDto createPayment(OrderDto orderDto);

    void markPaymentSuccess(UUID paymentId);

    void markPaymentFailed(UUID paymentId);
}
